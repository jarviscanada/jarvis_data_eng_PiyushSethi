#!/bin/bash

psql_host=$1
psql_port=$2
db_name=$3
psql_user=$4
psql_password=$5

if [ "$#" -ne 5 ]; then
        echo "Illegal number of parameters"
        exit 1
fi

hostname=$(hostname -f)
vmstat_mb=$(vmstat --unit M)
memory_free=$(vmstat --unit M | tail -1 | awk '{print $4}' | xargs)
cpu_idle=$(echo "$vmstat_mb" | tail -1 | awk '{print $15}' | xargs)
cpu_kernel=$(echo "$vmstat_mb" | tail -1 | awk '{print $14}' | xargs)
disk_io=$(vmstat -d | tail -1 | awk '{print $10}')
disk_available=$(df -BM / | tail -1 | awk '{print $4}' | sed 's/M//'| xargs)
timestamp=$(date "+%Y-%m-%d %H:%M:%S")


echo "hostname: $hostname"
echo "memory_free: $memory_free MB"
echo "cpu_idle: $cpu_idle%"
echo "cpu_kernel: $cpu_kernel"
echo "disk_io: $disk_io"
echo "disk_available: $disk_available MB"
echo "timestamp: $timestamp"


host_id=$(psql -h $psql_host -p $psql_port -U $psql_user -d $db_name -t -c "SELECT id FROM host_info WHERE hostname='$hostname';" | xargs)

insert_stmt="INSERT INTO host_usage(timestamp, host_id, memory_free, cpu_idle, cpu_kernel, disk_io, disk_available) VALUES('$timestamp', '$host_id', '$memory_free', '$cpu_idle', '$cpu_kernel', '$disk_io', '$disk_available');"

export PGPASSWORD=$psql_password
psql -h "$psql_host" -p "$psql_port" -U "$psql_user" -d "$db_name" -c "$insert_stmt"

exit $?
