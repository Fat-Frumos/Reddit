#!/bin/bash

# keytool -genkey -alias bootsecurity -storetype PKCS12 -keyalg RSA -keysize 2048 -keystore bootsecurity.p12 -validity 3650

openssl genpkey -algorithm RSA -out app.key -pkeyopt rsa_keygen_bits:2048
openssl rsa -in rsa_private_pkcs1.pem -pubout -out app.pub

openssl pkcs8 -topk8 -in rsa_private_pkcs1.pem -out rsa_private.pem -nocrypt