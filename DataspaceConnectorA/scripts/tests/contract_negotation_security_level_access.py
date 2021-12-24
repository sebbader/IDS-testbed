#!/usr/bin/env python3
#
# Copyright 2020 Fraunhofer Institute for Software and Systems Engineering
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#    http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.
#

import pprint
import requests
import sys

from idsapi import IdsApi
from resourceapi import ResourceApi

providerUrl = "http://localhost:8080"
consumerUrl = "http://localhost:8081"

provider_alias = "http://provider-dataspace-connector"
consumer_alias = "http://consumer-dataspace-connector"


def main(argv):
    if len(argv) == 2:
        provider_alias = argv[0]
        consumer_alias = argv[1]
        print("Setting provider alias as:", provider_alias)
        print("Setting consumer alias as:", consumer_alias)


if __name__ == "__main__":
    main(sys.argv[1:])

print("Starting script")

# Suppress ssl verification warning
requests.packages.urllib3.disable_warnings()

# Provider
provider = ResourceApi(providerUrl)

## Create resources
dataValue = "SOME LONG VALUE"
catalog = provider.create_catalog()
offers = provider.create_offered_resource()
representation = provider.create_representation()
artifact = provider.create_artifact(data={"value": dataValue})
contract = provider.create_contract()
use_rule = provider.create_rule(
    data={
        "value": """{
  "@context" : {
    "xsd" : "http://www.w3.org/2001/XMLSchema#",
    "ids" : "https://w3id.org/idsa/core/",
    "idsc" : "https://w3id.org/idsa/code/"
  },
  "@type" : "ids:Permission",
  "@id" : "https://w3id.org/idsa/autogen/permission/0c01b27b-8bf8-4844-bd2b-c8ebccd59676",
  "ids:description" : [ {
    "@value" : "security-level-restriction",
    "@type" : "http://www.w3.org/2001/XMLSchema#string"
  } ],
  "ids:title" : [ {
    "@value" : "Example Usage Policy",
    "@type" : "http://www.w3.org/2001/XMLSchema#string"
  } ],
  "ids:constraint" : [ {
    "@type" : "ids:Constraint",
    "@id" : "https://w3id.org/idsa/autogen/constraint/a2d9ac86-d3fc-4c28-b076-3ddcf0f7a371",
    "ids:operator" : {
      "@id" : "https://w3id.org/idsa/code/EQUALS"
    },
    "ids:leftOperand" : {
      "@id" : "https://w3id.org/idsa/code/SECURITY_LEVEL"
    },
    "ids:rightOperand" : {
      "@value" : "https://w3id.org/idsa/code/TRUST_SECURITY_PROFILE",
      "@type" : "xsd:string"
    }
  } ],
  "ids:action" : [ {
    "@id" : "https://w3id.org/idsa/code/USE"
  } ]
}"""
    }
)

## Link resources
provider.add_resource_to_catalog(catalog, offers)
provider.add_representation_to_resource(offers, representation)
provider.add_artifact_to_representation(representation, artifact)
provider.add_contract_to_resource(offers, contract)
provider.add_rule_to_contract(contract, use_rule)

print("Created provider resources")

# Consumer
consumer = IdsApi(consumerUrl)

# Replace localhost references
offers = offers.replace(providerUrl, provider_alias)
artifact = artifact.replace(providerUrl, provider_alias)

# IDS
# Call description
offer = consumer.descriptionRequest(provider_alias + "/api/ids/data", offers)
pprint.pprint(offer)

# Negotiate contract
obj = offer["ids:contractOffer"][0]["ids:permission"][0]
obj["ids:target"] = artifact
response = consumer.contractRequest(
    provider_alias + "/api/ids/data", offers, artifact, False, obj
)
pprint.pprint(response)

# Pull data
agreement = response["_links"]["self"]["href"]

consumerResources = ResourceApi(consumerUrl)
artifacts = consumerResources.get_artifacts_for_agreement(agreement)
pprint.pprint(artifacts)

first_artifact = artifacts["_embedded"]["artifacts"][0]["_links"]["self"]["href"]
pprint.pprint(first_artifact)

data = consumerResources.get_data(first_artifact).text
pprint.pprint(data)

expectedErorMessage = """{"message":"A policy restriction has been detected."}"""
if data != expectedErorMessage:
    exit(1)

exit(0)
