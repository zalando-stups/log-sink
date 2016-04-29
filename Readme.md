# Log Sink

[![codecov](https://codecov.io/gh/zalando-stups/logsink/branch/master/graph/badge.svg)](https://codecov.io/gh/zalando-stups/logsink)
[![Build Status](https://api.travis-ci.org/zalando-stups/logsink.svg?branch=master)](https://travis-ci.org/zalando-stups/logsink)

This application is supposed to serve as a central sink for audit- and traceability relevant log files in the STUPS ecosystem,
such as the taupage.yaml and logs files written by audit daemon in Taupage hosts.

The current implementation is only a proxy that forwards all matching requests to Fullstop.

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
