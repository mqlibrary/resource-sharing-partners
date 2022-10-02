# Usage
__[HOME](README.md)__

There are three actions that you can use with the application:
  1. Harvest (this is for harvesting data from the various source systems)
  2. Preview (this can show you what would change in Alma without changing anything)
  3. Sync (this updates Alma with the latest harvested partner data)

If you execute the application with a -h or -? you will see the following:
```bash
java -jar resource-partner-sharing-x.y.z.jar [options]
  -h                                        help
  -?                                        help
  -action (harvest|sync|preview)
      harvest                               run the harvest
      sync                                  sync partner data with Alma
      preview                               show what would happen without updating Alma
  -harvesters harvester1,hearvester2,...    allows selection of harvesters to run by specifying
                                            a comma separated list: [LADD,ILRS,TEPUNA,OUTLOOK]
                                            (only works with the harvest action)
```

## The Harvest Action
### Execution
To execute the __harvest__ action we run the following command:
```bash
rsp.cmd -action harvest
```

This will execute the harvester and all the harvesting modules, LADD, ILRS, TEPUNA, OUTLOOK.

We can also select specific harvesters to run by using the __-harvesters__ switch, e.g.
```bash
rsp.cmd -action harvest -harvesters LADD,TEPUNA
```
This would only run the harvesters for LADD and TEPUNA.

After the harvesting is complete, a file containing all field changes for any changed partners is created called __harvest-partners-changes.csv__. This allows you to see all changes to any partners in a convenient format.

### IRLS Harvesting
IRLS harvesting generates a config file in the config folder, __ILRS.json__.
```json
{
	"last_run_attempt": "2022-09-28T11:44:32+1000",
	"last_run": "2022-09-28T11:39:48+1000",
  "days_between_updates": "7"
}
```
This config records when the last run time and last run attempt occurred. The IRLS harvest only happens once every 7 days. This is the default setting. If you want to run it more frequently, change the __days_between_updates__ from the default 7 days to something more appropriate. Setting this value to 0 will cause the ILRS harvest to happen with every execution.


### The __previous__ folder
When a partner is updated via the __harvest__ action, the partner is first backed up to the __previous__ folder (under the __partners__ folder) before the updated record is saved. This allows you to do a comparison between the current and previous state of a partner (or even restore the previous state if so desired by simply copying the file in __previous__ to the parent folder). Note that all changes to the partner record from a harvest is saved to the __harvest-partners-changes-[timestamp].csv__.
## The Preview Action
To execute the __preview__ action we run the following command:
```bash
rsp.cmd -action preview
```

The __preview__ action:
1. Fetches all partners from the local datastore.
2. Fetches all partners from Alma.
3. Compares all partners between the local datastore and Alma.
4. Generates a file called __partners-changed-[timestamp].json__. This contains all the partners that require updating in Alma. The file is in the Alma update format.
5. Generates a file called __partners_changes-[timestamp].csv__. This is a CSV file that contains a list of all field changes for partners that have differences. 
6. Generates a file called __partners-deleted-[timestamp].json__. This contains all partners that are in Alma, but not in the local datastore. These could potentially be deleted. The application does not delete partner records, but highlights the ones that could be deleted (manually) in this file.

## The Sync Action
To execute the __sync__ action we run the following command:
```bash
rsp.cmd -action sync
```

The __sync__ action:
1. Fetches all partners from the local datastore.
2. Fetches all partners from Alma.
3. Compares all partners between the local datastore and Alma.
4. Generates a file called __partners-changed-[timestamp].json__. This contains all the partners that require updating in Alma. The file is in the Alma update format.
5. Generates a file called __partners_changes-[timestamp].csv__. This is a CSV file that contains a list of all field changes for partners that have differences. 
6. Generates a file called __partners-deleted-[timestamp].json__. This contains all partners that are in Alma, but not in the local datastore. These could potentially be deleted. The application does not delete partner records, but highlights the ones that could be deleted (manually) in this file.
7. Updates Alma with all partners that are different between the local datastore and Alma (the partners in the file __partners-changed-[timestamp].json__).

## Additional Features
### Partner Override
There might be some use cases where you would like to override the data sourced from the source systems. Perhaps the Address information in ILRS is incorrect and you would like to add the correct address to be synced.

You could do this with an __override file__. In the __data/partners__ folder, there is a folder called '__override__'. Simply copy the partner json file from the __data/partners__ folder to the __data/partners/override__ folder and edit the data you wish to change in the record in the __override__ folder. When syncing or previewing, if a partner record exists in the __override__ folder, this is the data that will be used instead of the record in the __data/partners__ folder.

### Block Alma Partner Updating
There may be situations where you wish to manually manage a partner in Alma. To prevent the partner from being updated by the __synchroniser__, you can add a __Note__ to the partner in Alma. Add a __Note__ with the text __NOSYNC__ to the partner and it will not be updated by the sync action.