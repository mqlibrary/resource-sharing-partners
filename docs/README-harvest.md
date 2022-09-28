# Resource Sharing Partner Data for Alma

#### Harvesting Resource Sharing Partner Data

![Harvesting Resource Sharing Partner Data](rsp-harvest-01.png)

# Installing the Infrastructure

## Prerequisites

1.  Java 8/11
1.  Maven 3.x
1.  Git
1.  RestAPI client application (e.g. Curl, HTTPie, Postman). For the examples we use [HTTPie](https://httpie.org)
1.  Alma API Key (R/W on Resource Sharing Partners API)
1.  Outlook Email Account (Office365 also supported, but need to get Organisation managers to create an 'Application')

## Process

Sample config files that are used in the documentation can be found [here](sample-config-files/).

1.  [Download, install and configure Elasticsearch.](install-elasticsearch.md)
    -   [YouTube: Installing Elasticsearch](https://www.youtube.com/watch?v=8Zhr9Nd8oSw)
1.  [Create the indexes on Elasticsearch.](create-elasticsearch-indexes.md)
    -   [YouTube: Resource Sharing Partners - Configure Indexes](https://youtu.be/QYBMUliLCPo)
1.  [Download Pre-Built Release](https://github.com/mqlibrary/resource-sharing-partners-harvest/releases) __OR__ [Clone, build, configure and run the harvester.](prepare-harvester.md)
    -   [YouTube: Resource Sharing Partners - Build, Deploy, Configure and Run the Harvester](https://youtu.be/Ak3Jcnihtp8)
1.  [Download Pre-Build Release](https://github.com/mqlibrary/resource-sharing-partners-sync/releases) __OR__ [Clone, build, configure and run the sync server.](prepare-sync.md)
    -   [YouTube: Resource Sharing Partners - Build, Deploy, Configure and Run the Sync Server](https://youtu.be/ALw_9pEUJPc)
1.  [Configure your institution.](configure-institution.md)
1.  [Interact with the sync server to perform various actions.](usage.md)

[YouTube Playlist - Resource Sharing Partners Configuration](https://www.youtube.com/playlist?list=PLr1gFE_jzVeYx922_wPtym2fTpAFIor53)

# Harvesting Data Concepts

## 1. Harvesting LADD (_Australia_)

This module scrapes from the following page: https://www.nla.gov.au/librariesaustralia/connect/find-library/ladd-members-and-suspensions/.

Harvester then extracts information regarding the:

1.  NUC symbol
1.  Organisation name
1.  ISO type
1.  Suspension status
1.  Suspension from - to dates

The following fields in the partner-records index are created/updated:

```json
{
    "nuc": "AAAR",
    "updated": "2018-01-22T13:05:07+1100",
    "name": "National Archives of Australia",
    "enabled": true,
    "iso_ill": false,
    "status": "suspended",
    "suspensions": [
        {
            "suspension_end": "2018-12-30T00:00:00+1100",
            "suspension_start": "2016-07-20T00:00:00+1000",
            "suspension_status": "suspended"
        }
    ]
}
```

## 2. Harvesting ILRS (_Australia_)

For each of the NUC symbols in the index, this module submits a search form to the ILRS web application and then processes the results. The web application is located at: http://www.nla.gov.au/apps/ilrs. From the search result we can extract the following information:

1.  Address information
    -   lines 1-3, city, state, postcode, country
    -   billing addresses, postal, main
1.  Contact information
    -   main email address
    -   ill email address
    -   main phone number
    -   ill phone number
    -   fax number

The ILRS has a configuration record in the partner-configs index, under 'ILRS', to assist with harvesting. We track when the last harvest was completed, and when the last one was attempted. As address information is not as volatile as suspension information, as well as the fact that the harvesting of ILRS is a resource intensive process for them, we limit the harvesting to 7 day intervals minimum - i.e. if the data was harvested less than 7 days ago, do not harvest again. The configuration record looks as follows:

```json
{
    "last_run": "2018-02-26T17:20:02+1100",
    "last_run_attempt": "2018-03-01T14:15:18+1100"
}
```

This module updates the following fields in the partner-records index:

```json
"email_main": null,
"email_ill": "library@naa.gov.au",
"phone_main": null,
"phone_ill": "02 6212 3683",
"phone_fax": "02 6212 3699",
"addresses": [
    {
        "address_detail": {
            "country": {
                "@desc": "Australia",
                "value": "AUS"
            },
            "city": "PARKES",
            "state_province": "ACT",
            "postal_code": "2600",
            "line2": "National Archives Building / Queen Victoria Terrace",
            "line1": "Ground Floor"
        },
        "address_type": "main",
        "address_status": "active"
    },
    {
        "address_detail": {
            "country": {
                "@desc": "Australia",
                "value": "AUS"
            },
            "city": "CANBERRA MAIL CENTRE",
            "state_province": "ACT",
            "postal_code": "2610",
            "line1": "PO Box 7425"
        },
        "address_type": "postal",
        "address_status": "active"
    },
    {
        "address_detail": {
            "line2": "Same as Postal address",
            "line1": "ATTN: Librarian"
        },
        "address_type": "billing",
        "address_status": "active"
    }
]
```

## 3. Harvesting TEPUNA (_New Zealand_)

This module harvests data from a maintained CSV file as the following location: https://natlib.govt.nz/directory-of-new-zealand-libraries.csv.
The following data is extracted from the CSV:

1.  NUC symbol
1.  Organisation name
1.  ISO type
1.  Address information
    -   lines 1-3, city, state, postcode, country
    -   billing addresses, postal, main
1.  Contact information
    -   main email address
    -   ill email address
    -   main phone number
    -   ill phone number
    -   fax number

For New Zealand partners, the following fields are updated/created:

```json
{
    "nuc": "NLNZ:ABI",
    "updated": "2018-01-22T13:54:28+1100",
    "name": "Deane Memorial Library",
    "enabled": true,
    "iso_ill": false,
    "status": "not suspended",
    "email_main": "circulation@laidlaw.ac.nz",
    "email_ill": "jadams@laidlaw.ac.nz",
    "phone_main": "+64 9 836 7800",
    "phone_ill": "+64 9 836 7800",
    "phone_fax": null,
    "addresses": [
        {
            "address_detail": {
                "country": {
                    "@desc": "New Zealand",
                    "value": "NZL"
                },
                "city": "Auckland",
                "postal_code": "0610",
                "line2": "Henderson",
                "line1": "80 Central Park Drive"
            },
            "address_type": "main",
            "address_status": "active"
        },
        {
            "address_detail": {
                "country": {
                    "@desc": "New Zealand",
                    "value": "NZL"
                },
                "city": "Auckland",
                "postal_code": "0650",
                "line2": "Henderson",
                "line1": "Private Bag 93 104"
            },
            "address_type": "postal",
            "address_status": "active"
        }
    ]
}
```

## 4. Harvesting EMAIL (_New Zealand_)

This module harvest emails from an Email address. Specifically, it is designed to connect to an Office365 Outlook account using OAuth2. The module reads all emails in the the Inbox and processes the relevant ones. Not all emails are relevant to this module as there are both suspension and address change email records coming through the system. The Tepuna harvester takes care of addresses so this modul only focusses on the suspension emails.

Each email is processed - data is extracted, compared with the partner-record index, and updated if necessary. The email is then moved to the 'Processed' folder in Outlook.

The Outlook configuration is stored in the partner-configs index under 'OUTLOOK'. Basically, we store the access token here and update it each time we log in:

```json
{
    "token_type": "Bearer",
    "scope": "Mail.ReadWrite https://graph.microsoft.com/User.Read",
    "expires_in": 3600,
    "ext_expires_in": 0,
    "access_token": "EwBAA8l6BAAURSN...",
    "refresh_token": "MCUN82zuoFJNOm...",
    "id_token": "eyJ0eXAiOiJKV1QiLCJ..."
}
```

For New Zealand partners, the following fields are updated/created:

```json
{
    "updated": "2018-01-22T13:54:28+1100",
    "suspensions": [
        {
            "suspension_added": "2017-06-19T14:21:35Z",
            "suspension_reason": "SUSPENDED_REQUESTING_OK",
            "suspension_end": "2017-06-23T00:00:00+1000",
            "suspension_start": "2017-06-19T00:00:00+1000",
            "suspension_code": "SUSPENDED_REQUESTING_OK",
            "suspension_status": "suspended"
        },
        {
            "suspension_added": "2017-06-24T02:58:51Z",
            "suspension_status": "not suspended"
        }
    ]
}
```
