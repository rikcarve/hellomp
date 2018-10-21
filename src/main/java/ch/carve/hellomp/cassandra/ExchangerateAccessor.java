package ch.carve.hellomp.cassandra;

import com.datastax.driver.core.LocalDate;
import com.datastax.driver.mapping.annotations.Accessor;
import com.datastax.driver.mapping.annotations.Query;

@Accessor
public interface ExchangerateAccessor {

    @Query("select * from exr.rates_query where baseCurrency = ? and toCurrency = ? AND effectiveFrom <= ? limit 1")
    Exchangerate getOne(int baseCurrency, int toCurrency, LocalDate date);

}
