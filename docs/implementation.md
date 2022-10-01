# Implementation
__[HOME](README.md)__

## Obtaining the Software

To obtain the software you could:
1. [Download](implementation.md#download) the pre-built software.
2. [Build](implementation.md#build) the software.

### Download
__Requirements:__
1. Java 11+.

__Downloading:__
1. Go to the [releases page](https://github.com/mqlibrary/resource-sharing-partners/releases) of the project.
2. Download the latest release of the software which looks like one of the below:
   - resource-sharing-partners-1.0.0-dist.tar.gz
   - resource-sharing-partners-1.0.0-dist.zip

### Build
__Requirements:__
1. Git.
2. Java 11+.
3. Apache Maven 3.6+.

__Building:__
1. Select a workspace (e.g. C:\Temp\rsp or /home/username/rsp). We will use __/opt__.
2. Checkout project from [GitHub](https://github.com/mqlibrary/resource-sharing-partners).
   ```bash
   cd /opt
   git clone https://github.com/mqlibrary/resource-sharing-partners
   cd resource-sharing-partners
   ```
3. Build the project with __Maven__:
   ```bash
   mvn -DskipTests -P prd clean package
   ```
4. This should create a package under the __target/__ folder:
   ```bash
   resource-sharing-partners-x.x.x-dist.(zip|tar.gz)
   ```

---
## Installation

The software is distributed as:
1. A single Java binary.
2. Wrapper scripts for running on either windows or linux (rsp.cmd or rsp.sh).
3. A bootstrap configuration file called __app.properties__ that you will need to configure before running the application.
4. A bootstrap configuration file called __ALMA.json__ that you will need to configure carefully before running the application.

### Installing the software
Once you have the package from either downloading or building, unzip/untar the appropriate package to desired location and maybe create an alias, e.g.:
```bash
cd /opt
tar xzf /opt/resource-sharing-partners-1.0.0-dist.tar.gz
ln -snf resource-sharing-partners-1.0.0 rsp
cd /opt/rsp
```

---
## Configuration

### Configure a proxy if required.
If you are behind a proxy, edit the file ```rsp.sh``` and replace the proxy variables with their appropriate values.

### Configure the properties file.
Edit the app.properties file.

```properties
location.config=data/config
location.partners=data/partners
location.output=data/output

ws.url.ilrs=http://www.nla.gov.au/apps/ilrs

ws.url.ladd=https://trove.nla.gov.au/partners/partner-services/resource-sharing/suspensions

ws.url.tepuna=https://natlib.govt.nz/librarians/directory-of-new-zealand-libraries.csv

outlook.url.endpoint=https://graph.microsoft.com/v1.0
outlook.url.token=https://login.microsoftonline.com/common/oauth2/v2.0/token
outlook.client.email=
outlook.client.id=
outlook.client.secret=
```

Most of these values can remain unchanged. You will however need to fill in the __outlook.client.*__ details with your account details.

The __location__ configuration specifies a location where:
- partner data will be stored
- config data will be stored
- output from executing syncing.

By default they will be under the execution folder (e.g. /opt/rsp in our example).
- /opt/rsp/
    - data/config
    - data/partners
    - data/output
    
### Configuring your Institution (__ALMA.json__).
The default data/config folder should already be there as part of the package you unzipped. It contains a sample Alma configuration file called __ALMA.json__. You will need to customise this file to reflect the configuration you have in Alma. Partners will be created/updated with the values you specify in this file.

 ___You need to go through this config extremely carefully!___

An Alma record is an amalgamation of the Datastore representation of an Organisation and the loading institutions configuration.

```
ALMA CONFIGURATION (ALMA.json) + PARTNER RECORD = ALMA RECORD
```

__Sample Alma configuration (__ALMA.json__) (_do not use with comments_)__:
```json
{
	// API base url - usually no need to change
	"apiurl": "https://api-ap.hosted.exlibrisgroup.com/almaws/v1",
	
	// Insert API key. Requires "Partners  Production Read/write" permissions.
	"apikey": "[enter your apikey here]",
	
	// This is where you put your NUC code for your institution
	"nuc": "NMQU",

	// Workflow settings
	"avgSupplyTime": 4,
	"borrowingSupported": true,
	"borrowingWorkflow": "LADD_Borrowing",
	"currency": "AUD",
	"deliveryDelay": 4,
	"lendingSupported": true,
	"lendingWorkflow": "LADD_Lending",
	"linkBase": "https://api-ap.hosted.exlibrisgroup.com/almaws/v1/partners/",
	"locateProfileDesc": "LADD Locate Profile",
	"locateProfileValue": "LADD",
	"systemTypeDesc": "LADD",
	"systemTypeValue": "LADD",

	// ISO configuration settings
	"isoAlternativeDocumentDelivery": false,
	"isoIllPort": "1611",
	"isoIllServer": "nla.vdxhost.com",
	"isoRequestExpiryTypeDesc": "Expire by interest date",
	"isoRequestExpiryTypeValue": "INTEREST_DATE",
	"isoSendRequesterInformation": false,
	"isoSharedBarcodes": true,
	"isoSymbol": "NLA:NMQU",

    // Some flags to allow setting of the 'preferred' option for contact info
	"preferredAddressType": "shipping",
	"preferredEmailType": "ill",
	"preferredPhoneType": "order_phone"
}
```

The following are valid preferred types:
- Address:
   - billing
   - claim
   - order
   - payment
   - returns
   - shipping
- Phone:
   - claimPhone
   - orderPhone
   - paymentPhone
   - returnsPhone
- Email:
   - claimMail
   - orderMail
   - paymentMail
   - queries
   - returnsMail

__Sample partner record:__
```json
{
	"nuc": "AATO",
	"updated": "2022-09-28T11:06:57+1000",
	"name": "Australian Taxation Office",
	"enabled": true,
	"iso_ill": false,
	"status": "suspended",
	"email_main": null,
	"email_ill": "library-NAT@ato.gov.au",
	"phone_main": null,
	"phone_ill": "(02) 62161500",
	"phone_fax": null,
	"suspensions": [
		{
			"suspension_added": null,
			"suspension_status": "suspended",
			"suspension_start": "2022-06-27T12:00:00Z",
			"suspension_end": "2022-12-31T12:00:00Z",
			"suspension_code": null,
			"suspension_reason": null
		}
	],
	"addresses": [
		{
			"address_status": "active",
			"address_type": "main",
			"line1": "ATO Library & Information Services",
			"line2": "26 Narellan St",
			"line3": null,
			"line4": null,
			"line5": null,
			"city": "Canberra",
			"state_province": "ACT",
			"postal_code": "2600",
			"country": "Australia",
			"address_note": null,
			"start_date": null,
			"end_date": null,
			"preferred": null
		},
		{
			"address_status": "active",
			"address_type": "postal",
			"line1": "Same as Main address",
			"line2": null,
			"line3": null,
			"line4": null,
			"line5": null,
			"city": null,
			"state_province": null,
			"postal_code": null,
			"country": null,
			"address_note": null,
			"start_date": null,
			"end_date": null,
			"preferred": null
		},
		{
			"address_status": "active",
			"address_type": "billing",
			"line1": "Same as Postal address",
			"line2": null,
			"line3": null,
			"line4": null,
			"line5": null,
			"city": null,
			"state_province": null,
			"postal_code": null,
			"country": null,
			"address_note": null,
			"start_date": null,
			"end_date": null,
			"preferred": null
		}
	]
}
```

__Sample Alma record:__
```json
{
	"partner_details": {
		"code": "AATO",
		"name": "Australian Taxation Office",
		"status": "INACTIVE",
		"profile_details": {
			"profile_type": "ISO",
			"iso_details": {
				"alternative_document_delivery": false,
				"ill_server": "nla.vdxhost.com",
				"ill_port": 1611,
				"iso_symbol": "NLA:AATO",
				"request_expiry_type": {
					"value": "INTEREST_DATE",
					"desc": "Expire by interest date"
				},
				"send_requester_information": false,
				"shared_barcodes": true,
				"ignore_shipping_cost_override": false
			}
		},
		"system_type": {
			"value": "LADD",
			"desc": "LADD"
		},
		"avg_supply_time": 4,
		"delivery_delay": 4,
		"currency": "AUD",
		"borrowing_supported": true,
		"borrowing_workflow": "LADD_Borrowing",
		"lending_supported": true,
		"lending_workflow": "LADD_Lending",
		"auto_cancel_supported": false,
		"auto_claim_supported": false,
		"locate_profile": {
			"value": "LADD",
			"desc": "LADD Locate Profile"
		},
		"holding_code": "AATO"
	},
	"contact_info": {
		"address": [
			{
				"line1": "ATO Library & Information Services",
				"line2": "26 Narellan St",
				"line3": null,
				"line4": null,
				"line5": null,
				"city": "Canberra",
				"state_province": "ACT",
				"postal_code": "2600",
				"country": {
					"value": "AUS",
					"desc": "Australia"
				},
				"start_date": "2022-08-16Z",
				"address_type": [
					"billing",
					"claim",
					"order",
					"payment",
					"returns",
					"shipping"
				],
				"preferred": true
			}
		],
		"phone": [
			{
				"phone_number": "(02) 62161500",
				"phone_type": [
					"claimPhone",
					"orderPhone",
					"paymentPhone",
					"returnsPhone"
				],
				"preferred": false,
				"preferredSMS": null
			}
		],
		"email": [
			{
				"email_address": "library-NAT@ato.gov.au",
				"description": null,
				"email_type": [
					"claimMail",
					"orderMail",
					"paymentMail",
					"queriesMail",
					"returnsMail"
				],
				"preferred": false
			}
		]
	},
	"note": [
	],
	"link": null
}
```
