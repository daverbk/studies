# Start a container
docker run --name jmc-music \
  -e POSTGRES_USER=dev \
  -e POSTGRES_PASSWORD=pass \
  -e POSTGRES_DB=music \
  -p 5432:5432 \
  -v $(pwd)/db:/docker-entrypoint-initdb.d \
  -v pg_data:/var/lib/postgresql/data \
  -d postgres:15

# Log in
# psql -U dev -d dev
