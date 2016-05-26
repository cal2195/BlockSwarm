# BlockSwarm [![Build Status](https://travis-ci.com/cal2195/BlockSwarm.svg?token=5T1yEDqMHWZzKFskCP5i&branch=master)](https://travis-ci.com/cal2195/BlockSwarm)

An Anonymous Distributed P2P File Sharing Protocol

## Prerequisites
* Minimum **Java 1.8.0_77**
  * Check your Java version `java -version`
  * Get the latest version here: https://java.com/en/download/
* Port **44446** Port Forwarded to your client

## How to Contribute
* Project Files above generated in **Netbeans 8.1**
* Using **JDK 1.8.0_77**

## Team Sites
* Current Plan: [Coggle.it Mindmap](https://coggle.it/diagram/VzYOcbFV9UUdzggN/ea02c7b0c56ca0dcd8813bf95fe771098f2d1ce78cbebcc4322b11c1cd93dd32)
* Slack: https://blockswarm.slack.com/

## Current Features
* Anonymous
* Distributed file sharing network - every user helps the network
* **BlockSites** - Websites served over the BlockSwarm Network
  * Currently only static sites


## How it works

#### Uploading Files
* Uploaded files are split into 1MB blocks
* Stored with **SHA1 hash** of original file
* These blocks are then distributed throughout the network

#### Peers
* Every Peer has a cache (minimum 10GB)
* Each Peer attempts to fill up their cache with blocks
* Uploads and Downloads are also part of the cache
* Once all the blocks of a file have been cached, they are assembled into the final file

#### Cluster
* Peers are organised into **Clusters**, with one **SuperNode**
* When a peer joins a cluster, they start caching to help the network

#### Block Redundancy
* Current goal is to have **at least 2 copies** of every block
* Allows for one peer to fail safely
* Given enough time, the network will heal and another peer can safely fail

#### BlockSites
* Original author of a BlockSite generates a **Key Pair** for that sites
* **Public Key** distributed with a **Signature** to verify future updates
* Updates to that site require new signatures using the original **private key**

## Upcoming Plans
### Short Term
* Files must match their hash to confirm download
* **Dynamic BlockSites** using a sync'd database (Javascript API)
* **Encryption** - All connections will be encrypted with **AES** backed by **RSA** encrypted keys
* **Private Files** - Encrypted with private keys
  * Will have a short lifespan

## BlockSwarm - Principles

#### Mission Statement

##### Core:
* Should be very object oriented.

##### Files:
* Should be encrypted, hashed and then stored as 1MB blocks.
* Shouldn't be able to decrypt without all blocks.
* Reassembled files must match hash before decrypting.
* Individual blocks should mean nothing - just *encrypted garbage*.

##### Peers:
* Adding a new peer should increase the health of the network.
* Caching should be set up so a new peer immediately helps others.
* A *no user* mode should be the default. **ie.** A node should default to just helping the network as soon as it joins - no config necessary.
* Abusive peers should be blocked from the network.

##### Networking:
* New files should be duplicated at least once as quickly as possible.
* New files should be distributed in a **super seeder** fashion.
* All connections should be encrypted.

##### Caching:
* Blocks should not be deleted unless necessary.
* Having a *full* cache at all times is the goal.
* Deleting the last copy of a block on the network should be avoided, as this would render the whole file useless.
* There should be **at least** two copies of a block at any point in time for redundancy.
* You shouldn't be able to tell if you've downloaded a file, or if you've just randomly cached it.

##### Encryption
* All blocks should be encrypted using keys.
* Private files should only be accessible by knowing the hash name and having the secret key.
* Blocks should only be decryptible once you have the entire file.


## F.A.Q.

##### So what does BlockSwarm do?
The network will be based on it's users, not a centralised tracker. The network will be almost impossible to collapse.

BlockSwarm will break up files into "Blocks" of a set size (eg. 1mb), and hash tables will maintain a database of what users have what blocks to make up a file. When a new user connects to the network for the first time, they will be connected to an other user and be given a copy of their has table. This will tell the new user all the files that the old user knows to exist. As users connect to eachother, their hash table will grow and so will the files availible to them as they are added to the network.

Files will be stored in the cache of every user, and complex algorithms will maintain files as long as they are in demand, and delete them if demand falls for a reasonable amount of time.

The beauty of blockswarm is that there is no central server to which all users must connect to, to find what files they can access. It's stored on every node, and as long as at least one other user is online, files can be downloaded.

##### When will BlockSwarm be ready for use?
As of writing this (March 18th 2016), BlockSwarm is very much in its infancy stages. There is currently no time frame assigned to the projects completion, but we promise we are passionate about this project and something will be made of it!
