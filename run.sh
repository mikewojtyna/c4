#!/bin/bash
docker run -it --rm -p 8082:8080 -v $(pwd)/src/main/resources:/usr/local/structurizr structurizr/lite
