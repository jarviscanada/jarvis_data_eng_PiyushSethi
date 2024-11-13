-- Create the host_info table if it does not exist
CREATE TABLE IF NOT EXISTS host_info (
    id SERIAL PRIMARY KEY,
    hostname VARCHAR(50) NOT NULL,
    cpu_number INTEGER NOT NULL,
    cpu_architecture VARCHAR(50) NOT NULL,
    cpu_model VARCHAR(100) NOT NULL,
    cpu_mhz NUMERIC(5,2) NOT NULL,
    l2_cache INTEGER,
    total_mem INTEGER NOT NULL,
    timestamp TIMESTAMP NOT NULL
);

-- Create the host_usage table if it does not exist
CREATE TABLE IF NOT EXISTS host_usage (
    id SERIAL PRIMARY KEY,
    host_id INTEGER REFERENCES host_info(id) ON DELETE CASCADE,
    memory_free INTEGER NOT NULL,
    cpu_idle INTEGER NOT NULL,
    cpu_kernel INTEGER NOT NULL,
    disk_io INTEGER NOT NULL,
    disk_available INTEGER NOT NULL,
    timestamp TIMESTAMP NOT NULL
);
