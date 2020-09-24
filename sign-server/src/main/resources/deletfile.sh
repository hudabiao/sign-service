#!/bin/sh
for path in `find /opt/upload -name contract_signed.pdf`
do
    echo $path
done
