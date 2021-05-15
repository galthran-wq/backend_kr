 
package com.flowershop.bus;

public interface QueryHandler<R, Q extends Query<R>> {

    R handle(Q query);

}
