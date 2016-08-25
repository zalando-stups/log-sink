# Log Sink

[![codecov](https://codecov.io/gh/zalando-stups/log-sink/branch/master/graph/badge.svg)](https://codecov.io/gh/zalando-stups/log-sink)
[![Build Status](https://api.travis-ci.org/zalando-stups/log-sink.svg?branch=master)](https://travis-ci.org/zalando-stups/log-sink)

This application is supposed to serve as a central sink for audit- and traceability relevant log files in the STUPS ecosystem,
such as the taupage.yaml and logs files written by audit daemon in Taupage hosts.

The current implementation is a proxy that forwards all matching requests to Fullstop. If a request contains a taupage.yaml file
it will send a corresponding event including the taupage.yaml data to zalando's audit system.

### Environment Variables

The following environment variables/spring properties are required.
 
   | Variable                  | Description                                          |
   | ------------------------- | ---------------------------------------------------- |
   |SECURITY_USER_NAME         | Basic Auth User Name                                 |
   |SECURITY_USER_PASSWORD     | Basic Auth Password                                  |
   |TOKENS_ACCESS_TOKEN_URI    | Auth Server URL                                      |
   |FULLSTOP_URL               | Fullstop URL                                         |
   |INSTANCE_LOGS_PROXY_URL    | Proxy URL                                            |
   |MANAGEMENT_PORT            | Spring Boot's management capabilities                |
   |MANAGEMENT_SECURITY_ENABLED| Spring Boot's management capabilities                |
   |CREDENTIALS_DIR            | oauth2 credentials directory                         |
   |AUDITTRAIL_EVENT_NAME      | Name of the event which will be sent to audit system |
   |AUDITTRAIL_EVENT_VERSION   | Version of the event schema                          |
   |AUDITTRAIL_EVENT_NAMESPACE | Namespace of that Event                              |
   |AUDITTRAIL_URL             | URL of the audit system                              | 

## License

Copyright © 2016 Zalando SE

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   [http://www.apache.org/licenses/LICENSE-2.0](http://www.apache.org/licenses/LICENSE-2.0)

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
