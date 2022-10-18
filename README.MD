# API
queryall / query
add/ update/ del

##
localhost:8081/api/{uri}

## query 
by Name
localhost:8081/api/coin/query?name="美元"

## add ,update 
ex: curl -X GET localhost:8081/api/coin/add -data '{"name":"元元","coin_code":"UU"}'

## run h2 database
java -cp h2-2.1.214.jar org.h2.tools.Server  
> port default 8082 
> -tcpPort {port}
