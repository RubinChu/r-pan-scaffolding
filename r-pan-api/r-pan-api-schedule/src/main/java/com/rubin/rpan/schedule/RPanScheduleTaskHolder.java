package com.rubin.rpan.schedule;

import java.io.Serializable;
import java.util.Objects;
import java.util.concurrent.ScheduledFuture;

public class RPanScheduleTaskHolder implements Serializable {

    private static final long serialVersionUID = 1444488140009722221L;

    private RPanScheduleTask rPanScheduleTask;

    private ScheduledFuture scheduledFuture;

    public RPanScheduleTaskHolder(RPanScheduleTask rPanScheduleTask, ScheduledFuture scheduledFuture) {
        this.rPanScheduleTask = rPanScheduleTask;
        this.scheduledFuture = scheduledFuture;
    }

    public RPanScheduleTaskHolder() {
    }

    public RPanScheduleTask getrPanScheduleTask() {
        return rPanScheduleTask;
    }

    public void setrPanScheduleTask(RPanScheduleTask rPanScheduleTask) {
        this.rPanScheduleTask = rPanScheduleTask;
    }

    public ScheduledFuture getScheduledFuture() {
        return scheduledFuture;
    }

    public void setScheduledFuture(ScheduledFuture scheduledFuture) {
        this.scheduledFuture = scheduledFuture;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RPanScheduleTaskHolder that = (RPanScheduleTaskHolder) o;
        return Objects.equals(rPanScheduleTask, that.rPanScheduleTask) &&
                Objects.equals(scheduledFuture, that.scheduledFuture);
    }

    @Override
    public int hashCode() {
        return Objects.hash(rPanScheduleTask, scheduledFuture);
    }

    @Override
    public String toString() {
        return "RPanScheduleTaskHolder{" +
                "rPanScheduleTask=" + rPanScheduleTask +
                ", scheduledFuture=" + scheduledFuture +
                '}';
    }

}
