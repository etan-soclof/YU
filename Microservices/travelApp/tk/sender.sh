#!/bin/bash
curl -d '{"line": "'"$(tail -n 1 $(find / -name *.csv))"'"}' -H 'Content-Type: application/json' http://tk.default:80/