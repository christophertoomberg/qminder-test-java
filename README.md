## Hamburger application

### Two endpoints:

```
/venues - returns basic data for all venues
```
```
/venues/{id} - returns more detailed data, including picture of the latest hamburger, if the venue has one
```

Since the **/venues/{id}** has to make many subsequent requests to get the analysed picture link, it is somewhat slow.

To get the analysed picture immediately, there could be a cron job that runs on application startup and
once a day for example, to retrieve all the data necessary and store it in a database. As a next step, every time the
cron job would run, it would add/update/delete entries in the database accordingly to have the latest data.