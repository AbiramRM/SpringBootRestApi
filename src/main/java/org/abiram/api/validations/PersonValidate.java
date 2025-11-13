package org.abiram.api.validations;

import org.abiram.api.model.AttendanceStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class PersonValidate {

    public ResponseEntity<String> validateAttendanceStatus(List<Map<String, Object>> attendanceList) {
        for (Map<String, Object> attendanceData : attendanceList) {
            String statusStr = (String) attendanceData.get("attendanceStatus");
            if (statusStr != null) {
                try {
                    AttendanceStatus.valueOf(statusStr.toUpperCase());
                } catch (IllegalArgumentException e) {
                    return ResponseEntity.badRequest().body(
                        "Invalid attendance status: " + statusStr + 
                        ". Valid values are: PRESENT, ABSENT, ON_LEAVE, HOLIDAY, LATE, WFH, UNKNOWN"
                    );
                }
            }
        }
        return null; // Return null when validation passes
    }
}

