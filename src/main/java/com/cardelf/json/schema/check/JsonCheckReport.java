package com.cardelf.json.schema.check;

import java.util.ArrayList;
import java.util.List;

/**
 * @author bluecrush
 */
public class JsonCheckReport implements CheckReport {

    /**
     * 失败信息
     */
    private List<ReportMessage> reportMessages;

    @Override
    public boolean isSuccess() {
        return this.reportMessages == null;
    }

    /**
     * 添加reportMessage
     */
    public void addReportMessages(ReportMessage reportMessage) {
        if (this.reportMessages == null) {
            this.reportMessages = new ArrayList<>();
        }
        this.reportMessages.add(reportMessage);
    }

    public void addReportMessages(List<ReportMessage> reportMessages) {
        if (this.reportMessages == null) {
            this.reportMessages = new ArrayList<>();
        }
        if (reportMessages != null) {
            this.reportMessages.addAll(reportMessages);
        }
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        if (reportMessages != null) {
            reportMessages.forEach(reportMessage -> {
                result.append(reportMessage);
                result.append("\n");
            });
        } else {
            return "PASS";
        }
        return result.toString();
    }

    public List<ReportMessage> getReportMessages() {
        return reportMessages;
    }
}
