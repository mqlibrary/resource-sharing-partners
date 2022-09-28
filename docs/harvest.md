# Partner Synchronisation with Alma - Harvesting Data Concepts

![Harvesting Resource Sharing Partner Data](rsp-harvest-01.png)

## 1. Harvesting LADD (_Australia_)

This module scrapes from the following page: https://www.nla.gov.au/librariesaustralia/connect/find-library/ladd-members-and-suspensions/.

Harvester then extracts information regarding the:

1.  NUC symbol
1.  Organisation name
1.  ISO type
1.  Suspension status
1.  Suspension from - to dates

The following fields in the partner records are created/updated:

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

For each of the NUC symbols in the partner folder, this module submits a search form to the ILRS web application and then processes the results. The web application is located at: http://www.nla.gov.au/apps/ilrs. From the search result we can extract the following information:

1.  Address information
    -   lines 1-3, city, state, postcode, country
    -   billing addresses, postal, main
1.  Contact information
    -   main email address
    -   ill email address
    -   main phone number
    -   ill phone number
    -   fax number

The ILRS has a configuration record in the configs folder, called __ILRS,json__, to assist with harvesting. We track when the last harvest was completed, and when the last one was attempted. As address information is not as volatile as suspension information, as well as the fact that the harvesting of ILRS is a resource intensive process for them, we limit the harvesting to 7 day intervals minimum - i.e. if the data was harvested less than 7 days ago, do not harvest again. This is configuraable via the interval setting. The configuration record looks as follows:

```json
{
    "interval_days": 7,
    "last_run": "2018-02-26T17:20:02+1100",
    "last_run_attempt": "2018-03-01T14:15:18+1100"
}
```

This module updates the following fields in a partner record:

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

Each email is processed - data is extracted, compared with the partner record, and updated if necessary. The email is then moved to the 'Processed' folder in Outlook.

The Outlook configuration is stored in the config folder and is called __OUTLOOK.json__. Basically, we store the access token here and update it each time we log in:

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
