# Auction-System

Java based distributed actution system using RMI and Jgroup based on a three-tier architecture:

    - Replicas class (can create multiple replicas on the fly with consistent data)
    - front end server to handle any client requests
    - client (seller and buyer) 
  
  - Features include:
    - active replication
    - user authentication (public/private key)
    - access control
    - secure communication (public/private key)
    
  
Users can 

  - register
  - login
  - create aution
    - Name 
    - Discritpion
    - min starting bid
    - reserve price
  - delete auction
  - view other auctions
  - bid on other auctions
  - etc

See diagram.pdf for more details
