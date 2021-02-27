#!/bin/bash

thrift -r --gen java common.thrift

cp gen-java/* ../src/ -rf
rm gen-java -rf