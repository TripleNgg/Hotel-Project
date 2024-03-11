#!/bin/bash
set -e

echo "sh is running"
aws --endpoint-url=http://localhost:4566 s3 mb s3://lakeside-hotel
java -jar likeSide-hotel-1.0.jar
