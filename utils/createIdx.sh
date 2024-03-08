#!/bin/bash

# MongoDB connection information
MONGODB_URI=$1
API_VERSION="1"
DATABASE_NAME="Factory"
COLLECTION_NAME="orders"

# Connect to MongoDB and create the index
mongosh "$MONGODB_URI" --apiVersion $API_VERSION <<EOF
use $DATABASE_NAME
db.$COLLECTION_NAME.createIndex({ 'deliveryDetails.shipment_id': 1 }, { name: 'deliveryDetails.shipment_id_1' })
db.$COLLECTION_NAME.createIndex({ userId: 1, orderDate: 1 }, { name: 'userId_1_orderDate_1' })
db.$COLLECTION_NAME.createIndex({ orderDate: 1 }, { name: 'orderDate_1' })
db.$COLLECTION_NAME.createIndex({ orderId: 1 }, { name: 'orderId_1', unique: true })
db.$COLLECTION_NAME.createIndex({ _id: 1 }, { name: '_id_' })
db.$COLLECTION_NAME.getIndexes()
EOF
