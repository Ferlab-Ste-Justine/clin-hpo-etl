# spark-submit example

./spark-submit \
--class bio.ferlab.clin.idx.hpo.HPO \
--master local[*] \
--conf "spark.es.nodes=http://localhost" \
--conf "spark.es.port=9200" \
--conf "spark.es.nodes.wan.only=true" \
--conf "spark.hadoop.fs.s3a.endpoint=http://localhost:9000" \
--conf "spark.hadoop.fs.s3a.access.key=minioadmin" \
--conf "spark.hadoop.fs.s3a.secret.key=minioadmin" \
--conf "spark.hadoop.fs.s3a.path.style.access=True" \
--conf "spark.hadoop.fs.s3a.impl=org.apache.hadoop.fs.s3a.S3AFileSystem" \
/home/adrianpaul/projects/clin-hpo-etl/target/scala-2.12/clin-hpo-etl.jar \
/home/adrianpaul/projects/obo-parser/src/output \
hpo_2021_08_18_v4

#### s3a://hpo/2021-08-18
#### /home/adrianpaul/projects/obo-parser/src/output
