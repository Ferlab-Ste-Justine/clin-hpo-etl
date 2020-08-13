package bio.ferlab.clin.idx.hpo;


import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Encoders;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.catalyst.encoders.RowEncoder$;
import org.junit.Before;
import org.junit.Test;
import scala.collection.JavaConversions;
import scala.collection.JavaConverters;
import scala.collection.immutable.Seq;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TestHPOSpec {
    private SparkSession spark;

    @Before
    public void init() {
        this.spark = SparkSession.builder()
                .master("local")
                .appName("TestHPOSpec")
                .getOrCreate();
    }

    @Test
    public void shouldFilterByAncestors() {
        final String path = this.getClass().getResource("/hpospecs_terms.json").getFile();

        final Dataset<Row> df = spark.read().json(path);
        final Dataset<HPO.HPOEntry> dataSet = df.as(Encoders.bean(HPO.HPOEntry.class));
        final List<String> listMatches = Arrays.asList("HP:0012639", "HP:0001626");

        Seq<String> matches = JavaConversions.asScalaBuffer(listMatches).toList();
        Dataset<HPO.CompactHPOEntry> filtered = HPO.filterByAncestors(dataSet, matches);
        System.out.println(filtered.count());
    }
}
