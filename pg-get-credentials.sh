export SECRET=pg-rental-app-user-db-secret
dbname=$(kubectl get secrets ${SECRET} -o jsonpath='{.data.database}' | base64 -D)
username=$(kubectl get secrets ${SECRET} -o jsonpath='{.data.username}' | base64 -D)
password=$(kubectl get secrets ${SECRET} -o jsonpath='{.data.password}' | base64 -D)

host=$(kubectl get secrets ${SECRET} -o jsonpath='{.data.host}' | base64 -D)
port=$(kubectl get secrets ${SECRET} -o jsonpath='{.data.port}' | base64 -D)

PGPASSWORD=$password psql -h $host -p $port -d $dbname -U $username