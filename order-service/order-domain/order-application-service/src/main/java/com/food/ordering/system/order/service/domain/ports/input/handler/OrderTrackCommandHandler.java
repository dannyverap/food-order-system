package com.food.ordering.system.order.service.domain.ports.input.handler;

import com.food.ordering.system.order.service.domain.dto.track.TrackOrderQuery;
import com.food.ordering.system.order.service.domain.dto.track.TrackOrderResponse;

public interface OrderTrackCommandHandler {

  TrackOrderResponse trackOrder(TrackOrderQuery trackOrderQuery);

}

