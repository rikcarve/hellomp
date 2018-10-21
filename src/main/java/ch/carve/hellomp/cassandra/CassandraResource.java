package ch.carve.hellomp.cassandra;

import java.lang.invoke.MethodHandles;

import javax.annotation.PostConstruct;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.LocalDate;
import com.datastax.driver.core.Session;
import com.datastax.driver.mapping.Mapper;
import com.datastax.driver.mapping.MappingManager;

@Path("cassandra")
public class CassandraResource {

    private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private ExchangerateAccessor exr;
    private Mapper<Exchangerate> mapper;

    @PostConstruct
    public void init() {
        Cluster cluster = Cluster.builder().addContactPoint("localhost").build();
        Session session = cluster.connect();
        MappingManager manager = new MappingManager(session);
        exr = manager.createAccessor(ExchangerateAccessor.class);
        mapper = manager.mapper(Exchangerate.class);
        logger.info("cassandra session initalized");
    }

    @GET
    @Path("exr")
    @Produces("application/json")
    public Exchangerate exchangerate(@QueryParam("base") int base, @QueryParam("to") int to) {
        return exr.getOne(base, to, LocalDate.fromMillisSinceEpoch(System.currentTimeMillis()));
    }
    
    @PUT
    @Path("exr")
    @Consumes("application/json")
    public Response insert(Exchangerate rate) {
        if (rate != null) {
            rate.setEffectiveFrom(LocalDate.fromMillisSinceEpoch(System.currentTimeMillis()));
            mapper.save(rate);
        }
        return Response.ok().build();
    }
}
