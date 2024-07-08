package cz.cablo.knuspr.db

import cz.cablo.knuspr.bean.OrderItemError

class OrderItemException(val itemErrors: List<OrderItemError>) : RuntimeException() {}