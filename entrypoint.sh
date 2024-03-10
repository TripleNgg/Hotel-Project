#!/bin/bash
aws --endpoint-url=http://localhost:4566 s3 mb s3://lakeside-hotel
java -jar likeSide-hotel-0.0.1-SNAPSHOT.jar
