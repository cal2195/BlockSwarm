# BlockSwarm
© Cal Martin & Cillian Roe 2016

**Please note, that until version v1.0 anything may change with no backwards compatibility, so all information on the network may be wiped!**

### Don't forget:
* Keep it simple.
* Use **objects**.
* You're making it too complicated. :)

ERROR 404:
README not found...

### BlockSwarm - Principles

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


#### F.A.Q.

##### What is BlockSwarm?
Blockswarm is the new face on online P2P filesharing! As demonstrated in recent years, torrenting websites are gaining in popularity in unforseen ways; but are still at the mercy of governments and corporations such and the Music and Film industry.

Centralised trackers such as The Pirate Bay and KickAss torrents hold the network together, but the entire system crumbles when one of these vital pieces of infastructure fails... this makes us sad :'(

##### So what does BlockSwarm do?
BlockSwarm is the brain-child of Cal Martin (With random bits of idea input from Cillian). It's inspired by the node based P2P network of FreeNet. BlockSwarm will be a new answer to sharing and downloading.

The network will be based on it's users, not a centralised tracker. The network will be almost impossible to collapse.

BlockSwarm will break up files into "Blocks" of a set size (eg. 1mb), and hash tables will maintain a database of what users have what blocks to make up a file. When a new user connects to the network for the first time, they will be connected to an other user and be given a copy of their has table. This will tell the new user all the files that the old user knows to exist. As users connect to eachother, their hash table will grow and so will the files availible to them as they are added to the network.

Files will be stored in the cache of every user, and complex algorithms will maintain files as long as they are in demand, and delete them if demand falls for a reasonable amount of time.

The beauty of blockswarm is that there is no central server to which all users must connect to, to find what files they can access. It's stored on every node, and as long as at least one other user is online, files can be downloaded.

##### When will BlockSwarm be ready for use?
As of writing this (March 18th 2016), BlockSwarm is very much in its infancy stages. There is currently no time frame assigned to the projects completion, but we promise we are passionate about this project and somethign will be made of it!


