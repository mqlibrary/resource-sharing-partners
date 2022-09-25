package org.nishen.resourcepartners.util;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Reader;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.bootstrap.DOMImplementationRegistry;
import org.w3c.dom.ls.DOMImplementationLS;
import org.w3c.dom.ls.LSSerializer;

public class DataUtil
{
	private static final Logger log = LoggerFactory.getLogger(DataUtil.class);

	public static Document getDocumentFromFile(String filename) throws Exception
	{
		byte[] document = loadFile(filename);

		return getDocumentFromBytes(document);
	}

	public static Document getDocumentFromFile(File file) throws Exception
	{
		byte[] document = loadFile(file);
		document = (new String(document)).trim().getBytes();

		Document doc = getDocumentBuilder().parse(new ByteArrayInputStream(document));

		return doc;
	}

	public static Document getNewDocument() throws Exception
	{
		return getDocumentBuilder().newDocument();
	}

	public static Document getDocumentFromBytes(byte[] data) throws Exception
	{
		byte[] docBytes = (new String(data)).trim().getBytes("UTF-8");
		Document doc = getDocumentBuilder().parse(new ByteArrayInputStream(docBytes));

		return doc;
	}

	public static byte[] loadFile(String filename) throws IOException
	{
		File file = new File(filename.trim());
		return loadFile(file);
	}

	public static byte[] loadFile(File file) throws IOException
	{
		if (!file.exists() || !file.canRead())
		{
			String message = "Cannot read file: " + file.getCanonicalPath();
			System.err.println(message);
			throw new IOException(message);
		}

		FileInputStream fis = new FileInputStream(file);
		ByteArrayOutputStream data = new ByteArrayOutputStream();
		int len = 0;
		byte[] buf = new byte[1024];
		while ((len = fis.read(buf)) >= 0)
			data.write(buf, 0, len);
		fis.close();

		return data.toByteArray();
	}

	public static void saveFile(String filename, byte[] data) throws FileNotFoundException, IOException
	{
		File file = new File(filename);
		saveFile(file, data);
	}

	public static void saveFile(File file, byte[] data) throws FileNotFoundException, IOException
	{
		ByteArrayInputStream input = new ByteArrayInputStream(data);
		saveFile(file, input);
	}

	public static void saveFile(File file, InputStream input) throws FileNotFoundException, IOException
	{
		OutputStream fos = new FileOutputStream(file);
		int len = 0;
		byte[] buf = new byte[1024];
		while ((len = input.read(buf)) >= 0)
			fos.write(buf, 0, len);
		fos.close();
	}

	public static void saveDocument(String filename, byte[] document) throws Exception
	{
		try
		{
			File file = new File(filename.trim());
			String data = format(document);
			PrintWriter writer = new PrintWriter(file, "UTF-8");
			writer.print(data);
			writer.flush();
			writer.close();
		}
		catch (Exception e)
		{
			String message = "Unable to save file: " + filename;
			log.error("{}", message, e);
			throw new Exception(message, e);
		}
	}

	public static void saveDocument(String filename, Document doc) throws Exception
	{
		try
		{
			String data = format(doc);
			saveDocument(filename, data.getBytes("UTF-8"));
		}
		catch (Exception e)
		{
			String message = "Unable to save file: " + filename;
			log.error("{}", message, e);
			throw new Exception(message, e);
		}
	}

	public static String format(Document document, boolean declaration)
	{
		try
		{
			final DOMImplementationRegistry registry = DOMImplementationRegistry.newInstance();
			final DOMImplementationLS impl = (DOMImplementationLS) registry.getDOMImplementation("LS");
			final LSSerializer writer = impl.createLSSerializer();

			writer.getDomConfig().setParameter("format-pretty-print", true);
			writer.getDomConfig().setParameter("xml-declaration", declaration);

			return writer.writeToString(document);
		}
		catch (Exception e)
		{
			throw new RuntimeException(e);
		}
	}

	public static String format(Document document) throws Exception
	{
		return format(document, false);
	}

	public static String format(byte[] document) throws Exception
	{
		try
		{
			Document doc = getDocumentFromBytes(document);

			return format(doc);
		}
		catch (Exception e)
		{
			throw new RuntimeException(e);
		}
	}

	public static String format(String xml) throws Exception
	{
		return format(xml.getBytes("UTF-8"));
	}

	private static DocumentBuilder getDocumentBuilder() throws Exception
	{
		DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
		documentBuilderFactory.setNamespaceAware(true);
		DocumentBuilder docBuilder = documentBuilderFactory.newDocumentBuilder();

		return docBuilder;
	}

	public static String getHash(byte[] data) throws NoSuchAlgorithmException
	{
		MessageDigest digest = MessageDigest.getInstance("MD5");
		byte[] hash = digest.digest(data);

		String hexHash = byte2hex(hash);

		return hexHash;
	}

	public static byte[] clean(byte[] data) throws IOException
	{
		InputStream input = new ByteArrayInputStream(data);
		InputStreamReader reader = new InputStreamReader(input, "UTF8");
		Reader in = new BufferedReader(reader);

		ByteArrayOutputStream output = new ByteArrayOutputStream();
		OutputStreamWriter writer = new OutputStreamWriter(output);

		int ch;
		while ((ch = in.read()) > 1)
			writer.write(((char) ch) == '\uFFFD' ? '?' : ch);

		writer.close();
		output.close();
		in.close();

		return output.toByteArray();
	}

	/**
	 * Converts a hash into its hexadecimal string representation.
	 * 
	 * @param bytes the byte array to convert
	 * @return the hexadecimal string representation
	 */
	private static String byte2hex(byte[] bytes)
	{
		char[] hexChars = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };

		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < bytes.length; i++)
		{
			sb.append(hexChars[(bytes[i] >> 4) & 0xf]);
			sb.append(hexChars[bytes[i] & 0xf]);
		}

		return sb.toString();
	}
}
