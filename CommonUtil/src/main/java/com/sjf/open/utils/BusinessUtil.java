package com.sjf.open.utils;

import com.google.common.base.Objects;
import com.qunar.mobile.innovation.behavior.BookBehavior;
import com.qunar.mobile.innovation.behavior.ClickBehavior;
import com.qunar.mobile.innovation.behavior.FavoritesAddingBehavior;
import com.qunar.mobile.innovation.behavior.MobileUserBehavior;
import com.qunar.mobile.innovation.behavior.OrderBehavior;
import com.qunar.mobile.innovation.behavior.PvBehavior;
import com.qunar.mobile.innovation.behavior.coach.CoachBehavior;
import com.qunar.mobile.innovation.behavior.flight.FlightBehavior;
import com.qunar.mobile.innovation.behavior.hotel.HotelBehavior;
import com.qunar.mobile.innovation.behavior.search.SuggestionBehavior;
import com.qunar.mobile.innovation.behavior.ticket.TicketBehavior;
import com.qunar.mobile.innovation.behavior.train.TrainBehavior;
import com.qunar.mobile.innovation.behavior.travelguide.TravelGuideBehavior;
import com.qunar.mobile.innovation.behavior.vacation.VacationBehavior;
import com.sjf.open.model.ActionType;
import com.sjf.open.model.BusinessType;

/**
 * Created by xiaosi on 17-1-16.
 */
public class BusinessUtil {

    /**
     * 获取业务线
     * 
     * @param behavior
     * @return
     */
    public static BusinessType getBusinessLine(MobileUserBehavior behavior) {

        BusinessType businessType = null;
        if (Objects.equal(behavior, null)) {
            return businessType;
        }

        if (behavior instanceof FlightBehavior) {
            businessType = BusinessType.FLIGHT;
        } else if (behavior instanceof HotelBehavior) {
            businessType = BusinessType.HOTEL;
        } else if (behavior instanceof TrainBehavior) {
            businessType = BusinessType.TRAIN;
        } else if (behavior instanceof TicketBehavior) {
            businessType = BusinessType.TICKET;
        } else if (behavior instanceof VacationBehavior) {
            businessType = BusinessType.VACATION;
        } else if (behavior instanceof SuggestionBehavior) {
            businessType = BusinessType.SUGGESTION;
        } else if (behavior instanceof CoachBehavior) {
            businessType = BusinessType.COACH;
        } else if (behavior instanceof TravelGuideBehavior) {
            businessType = BusinessType.TRAVEL_GUIDE;
        }
        return businessType;

    }

    /**
     * 获取行为类型
     * @param behavior
     * @return
     */
    public static ActionType getActionType(MobileUserBehavior behavior) {

        ActionType actionType = null;
        if (Objects.equal(actionType, null)) {
            return actionType;
        }

        if (behavior instanceof ClickBehavior) {
            actionType = ActionType.CLICK;
        } else if (behavior instanceof PvBehavior) {
            actionType = ActionType.SEARCH;
        } else if (behavior instanceof OrderBehavior) {
            actionType = ActionType.ORDER;
        } else if (behavior instanceof FavoritesAddingBehavior) {
            actionType = ActionType.FAVORITE;
        } else if (behavior instanceof BookBehavior) {
            actionType = ActionType.BOOK;
        }
        return actionType;

    }

}
