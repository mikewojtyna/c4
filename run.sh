#!/bin/bash
docker run -it --rm -p 8080:8080 -v $(pwd)/src/main/resources:/usr/local/structurizr structurizr/structurizr local