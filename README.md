Java application part of the master's thesis titled "Telemetry Monitoring in Industry 4.0: A comparative Analysis of NoSQL-based Solutions with Focus on MongoDB and InfluxDB". Sapienza, Università di Roma - Faculty of Information Engineering, Informatics and Statistics. Master of Science in Engineering in Computer Science.

Project realized by [Antonio Ionta](https://www.linkedin.com/in/antonio-ionta) with the collaboration of [Exaltech S.r.l.](https://www.exaltech.it) and the supervision of [prof. Domenico Lembo](http://www.diag.uniroma1.it/~lembo/).

# Abstract

The Java application is able to fulfill different kind of tasks, that we can conceptually subdivide into two main categories:

- Data Porting
- Benchmarking

Within each of them there are other subtasks to be performed that you should select and initialize through a menu-guided CLI interaction during application execution.

# Prerequisites

You have to specify a command-line argument when launching the Java application (via CLI or through your favorite IDE) to indicate which of the above-mentioned tasks the application should fulfill:

- "p" for Data Porting
- "b" for Benchmarking

To interact with local and/or remote instances of the involved DBMSs you need to provide within the `resources` directory of the project a file named `secret.properties` with the following structure

```
# MongoDB
mongodb.remote.uri=<connection string>
mongodb.local.uri=<connection string>

# InfluxDB
influxdb.local.token=<token for API access>
```

Additionally, there are test cases in benchmarking modality which require providing within the `resources` directory of the project a file named `sensor_data_52-propByItem.jsonl` with the following structure

```
{"itemId":<long>,"properties":["<string>", "<string>", [...]]}
{"itemId":<long>,"properties":["<string>", "<string>", [...]]}

[...]
```