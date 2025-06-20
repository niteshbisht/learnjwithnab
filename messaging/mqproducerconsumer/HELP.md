# Getting Started

Hello Doston 

aaj hum sikhenge Ek Highly Available system jo ki high volume requests ko 
process karta hai aur assure karta hai bina kisi downtime k.

Iske lia hum Active MQ messaging broker ka use karenge.

Humara Aaj ka system ek Spring Rest API hai jo order receive karte hai 
aur order information ko MQ server p post kr dega.


Pehla Step:
MQ Server Start karte hai 

step1: podman pull apache/activemq-classic
step2: podman run -d --name activemq -p 8161:8161 -p 61616:61616 apache/activemq-classic
