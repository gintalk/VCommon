#!/bin/bash

thrift -r --gen java common.thrift
thrift -r --gen java web.thrift

cp gen-java/* ../src/ -rf
rm gen-java -rf