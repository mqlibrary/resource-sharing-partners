# Partner Synchronisation with Alma - Outlook Configuration
__[HOME](README.md)__

## Outlook Configuration
The first step is to create an Outlook account if you do not already have one. It is also possible to use a Microsoft365 institution account, but this will require some work to be done by your IT team that manages the Microsoft365 __'Organisation'__ to configure an __'App Registration'__. The Outlook account is required to receive Tepuna Status emails.

The second step is to create an __'App Registration'__. This enables programmatic access to the Outlook account so that the harvesting application can access the account and read/move emails.

We will cover both these tasks in detail.

## Creating an Outlook Account
You can simply go to https://outlook.live.com and create a new account. Select the Create account link in the top right corner.

![Creating an Outlook account](aoutlook-oauth2/outlook-oauth2-01.png)

![New Outlook account welcome screen](aoutlook-oauth2/outlook-oauth2-02.png)

Once you've created your account and logged in, you will need to create a new folder called 'Processed'. This is where Tepuna emails are moved once they are processed.
![New Outlook account Inbox](aoutlook-oauth2/outlook-oauth2-03.png)

## Creating an App Registration
Creating an __App Registration__ allows the harvest application to interact programmatically with the Outlook account via the [Microsoft Graph API](https://learn.microsoft.com/en-us/outlook/rest/get-started).

Some additional links:
- [OAuth2 using Microsoft AD](https://learn.microsoft.com/en-us/azure/active-directory/develop/)
- [Registering an Application](https://learn.microsoft.com/en-us/azure/active-directory/develop/quickstart-register-app)
- [Azure Portal for managing App Registrations](https://portal.azure.com/)

