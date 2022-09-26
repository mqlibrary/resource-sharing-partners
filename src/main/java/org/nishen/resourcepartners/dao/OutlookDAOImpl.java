package org.nishen.resourcepartners.dao;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.MediaType;

import org.nishen.resourcepartners.entity.AuthRefreshResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.name.Named;

public class OutlookDAOImpl implements OutlookDAO
{
	private static final Logger log = LoggerFactory.getLogger(OutlookDAOImpl.class);

	private static final String SOURCE_SYSTEM = "OUTLOOK";

	private static final String PROCESSED_FOLDER_NAME = "Processed";

	private ObjectMapper mapper;

	private Config config;

	private String clientId;

	private String clientSecret;

	private String email;

	private WebTarget outlook;

	private WebTarget outlookToken;

	private String accessToken;

	private String processedFolderId;

	@Inject
	public OutlookDAOImpl(ConfigFactory configFactory, @Named("outlook.client.email") String email,
	                      @Named("outlook.client.id") String clientId,
	                      @Named("outlook.client.secret") String clientSecret,
	                      @Named("ws.outlook") Provider<WebTarget> outlookWebTarget,
	                      @Named("ws.outlook.token") Provider<WebTarget> outlookTokenWebTarget) throws Exception
	{
		this.mapper = new ObjectMapper();
		this.config = configFactory.fetch(SOURCE_SYSTEM);
		this.clientId = clientId;
		this.clientSecret = clientSecret;
		this.email = email;
		this.outlook = outlookWebTarget.get();
		this.outlookToken = outlookTokenWebTarget.get();
		this.accessToken = getAccessToken();
		this.processedFolderId = getProcessedFolderId();

		log.debug("instantiated class: {}", this.getClass().getName());
	}

	public String getProcessedFolderId() throws Exception
	{
		String response = outlook.path("users")
		                         .path(email)
		                         .path("MailFolders")
		                         .queryParam("$top", 50)
		                         .request()
		                         .header("Authorization", "Bearer " + accessToken)
		                         .header("Prefer", "outlook.body-content-type=\"text\"")
		                         .accept(MediaType.APPLICATION_JSON)
		                         .get(String.class);

		log.debug("folders:\n{}", response);

		try
		{
			JsonNode root = mapper.readTree(response);
			for (JsonNode entry : root.get("value"))
			{
				String id = entry.get("id").asText();
				String name = entry.get("displayName").asText();
				if (PROCESSED_FOLDER_NAME.equals(name))
					return id;
			}
		}
		catch (Exception e)
		{
			log.error("unable to obtain processed folderId: {}", PROCESSED_FOLDER_NAME);
			log.debug("", e);
		}

		return null;
	}

	public void markMessagesProcessed(Map<String, JsonNode> messages)
	{
		if (processedFolderId == null)
		{
			log.error("unable to move emails to processed folder[{}]: {}", PROCESSED_FOLDER_NAME, processedFolderId);
			return;
		}

		String body = "{ \"DestinationId\": \"" + processedFolderId + "\" }";
		for (String id : messages.keySet())
		{
			WebTarget t = outlook.path("users")
			                     .path(email)
			                     .path("MailFolders")
			                     .path("Inbox")
			                     .path("messages")
			                     .path(id)
			                     .path("move");

			Builder request = t.request();
			request = request.header("Authorization", "Bearer " + accessToken);
			request = request.accept(MediaType.APPLICATION_JSON);
			String response = request.post(Entity.json(body), String.class);
			log.debug("move response: {}", response);
		}
	}

	public Map<String, JsonNode> getMessages()
	{
		Map<String, JsonNode> messages = new LinkedHashMap<String, JsonNode>();

		int batchSize = 50;
		int skip = 0;
		boolean hasMore = true;
		while (hasMore)
		{
			WebTarget t = outlook.path("users").path(email).path("MailFolders").path("Inbox").path("messages");
			t = t.queryParam("$select", "ReceivedDateTime,Body");
			t = t.queryParam("$filter",
			                 "ReceivedDateTime ge 1900-01-01T00:00:00Z and Subject eq 'ISO-ILL Location Updates'");
			t = t.queryParam("$orderby", "ReceivedDateTime");
			t = t.queryParam("$top", batchSize);
			t = t.queryParam("$skip", skip);

			Builder request = t.request();
			request = request.header("Authorization", "Bearer " + accessToken);
			request = request.header("Prefer", "outlook.body-content-type=\"text\"");
			request = request.accept(MediaType.APPLICATION_JSON);

			log.debug("request: {}", request.toString());

			String response = request.get(String.class);
			log.debug("messages:\n{}", response);

			try
			{
				JsonNode root = mapper.readTree(response);
				log.debug("next: {}", root.get("@odata.nextLink"));

				hasMore = root.has("@odata.nextLink");

				for (JsonNode entry : root.get("value"))
				{
					String id = entry.get("id").asText();
					messages.put(id, entry);
				}
			}
			catch (Exception e)
			{
				log.error("error processing json response: {}", response);
			}

			skip += batchSize;
		}

		return messages;
	}

	public String getAccessToken() throws Exception
	{
		String refreshToken =
		        config.get("refresh_token").orElseThrow(() -> new Exception("could not get refresh_token"));

		Form tokenForm = new Form().param("client_id", clientId)
		                           .param("refresh_token", refreshToken)
		                           .param("grant_type", "refresh_token")
		                           .param("client_secret", clientSecret);

		String authRefreshResponseData = outlookToken.request(MediaType.APPLICATION_FORM_URLENCODED)
		                                             .accept(MediaType.TEXT_HTML)
		                                             .post(Entity.form(tokenForm), String.class);

		log.debug("response: {}", authRefreshResponseData);

		AuthRefreshResponse authRefreshResponse = mapper.readValue(authRefreshResponseData, AuthRefreshResponse.class);

		Map<String, String> token = new HashMap<String, String>();
		token.put("token_type", authRefreshResponse.getTokenType());
		token.put("scope", authRefreshResponse.getScope());
		token.put("expires_in", String.valueOf(authRefreshResponse.getExpiresIn()));
		token.put("ext_expires_in", String.valueOf(authRefreshResponse.getExtExpiresIn()));
		token.put("access_token", authRefreshResponse.getAccessToken());
		token.put("refresh_token", authRefreshResponse.getRefreshToken());
		token.put("id_token", Optional.ofNullable(authRefreshResponse.getIdToken()).orElse(""));

		config.setAll(token);

		return authRefreshResponse.getAccessToken();
	}
}
