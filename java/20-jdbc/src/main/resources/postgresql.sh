# Start a container
docker run --name my-postgres \
  -e POSTGRES_USER=dev \
  -e POSTGRES_PASSWORD=pass \
  -p 5432:5432 \
  -v pg_data:/var/lib/postgresql/data \
  -d postgres:15

# Log in
psql -U dev -d dev
