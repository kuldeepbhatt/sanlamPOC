package com.sanlam.SanlamPOC.controller;
import com.sanlam.SanlamPOC.constants.BusinessConstants;
import com.sanlam.SanlamPOC.constants.ExceptionConstants;
import com.sanlam.SanlamPOC.exception.BadRequestException;
import com.sanlam.SanlamPOC.exception.BusinessException;
import com.sanlam.SanlamPOC.utility.Condition;
import com.sanlam.SanlamPOC.utility.QueryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import java.math.BigDecimal;
import com.sanlam.SanlamPOC.WithdrawalEvent;
//import software.amazon.awssdk.regions.Region;
//import software.amazon.awssdk.services.sns.SnsClient;
//import software.amazon.awssdk.services.sns.model.PublishRequest;
//import software.amazon.awssdk.services.sns.model.PublishResponse;

@RestController
@RequestMapping("/bank")
public class BankAccountController {
    @Autowired
    private JdbcTemplate jdbcTemplate;
    private static final Logger logger = org.slf4j.LoggerFactory.getLogger(BankAccountController.class);
    private SnsClient snsClient;
    public BankAccountController() {
        this.snsClient = SnsClient.builder()
            .region(Region.YOUR_REGION); // Specify your region .build();
    }

    @PostMapping("/withdraw")
    public String withdraw(@RequestParam("accountId") Long accountId,
                           @RequestParam("amount") BigDecimal amount) {
        try {
            String sql = new QueryBuilder(new Condition("balance", "id", "?", "accounts")).buildSelectQuery();
            //Note: queryForObject is deprecated
            BigDecimal currentBalance = jdbcTemplate.queryForObject(
                    sql, new Object[]{accountId}, BigDecimal.class);
            if (accountId == null || currentBalance == null || amount == null) {
                throw new BadRequestException(ExceptionConstants.BAD_REQUEST);
            } else {
                if (currentBalance.compareTo(amount) >= 0) {
                    String forValue = currentBalance.toString() + "- ?";
                    sql = new QueryBuilder(new Condition("balance", "id", forValue, "accounts")).buildUpdateQuery();
                    int rowsAffected = jdbcTemplate.update(sql, amount, accountId);
                    if (rowsAffected > 0) {
                        this.publishTransationStatusWith(amount, accountId, BusinessConstants.WITHDRAWAL_SUCCESSFUL);
                        return BusinessConstants.WITHDRAWAL_SUCCESSFUL;
                    } else {
                        //TODO:  probable extention: There should be segregation between the system failures and business logic failures
                        this.publishTransationStatusWith(amount, accountId, BusinessConstants.WITHDRAWAL_FAILED);
                        return BusinessConstants.WITHDRAWAL_FAILED;
                    }
                } else {
                    this.publishTransationStatusWith(amount, accountId, BusinessConstants.INSUFFICIENT_FUNDS);
                    return BusinessConstants.INSUFFICIENT_FUNDS;
                }
            }
        } catch (BadRequestException exception) {
            logger.error(exception.getMessage(), exception);
            return exception.getMessage();
        }
    }

    String publishTransationStatusWith(BigDecimal amount, Long accountId, String status) {
        try {
            WithdrawalEvent event = new WithdrawalEvent(amount, accountId, status);
            String eventJson = event.toJson();
            String snsTopicArn = "arn:aws:sns:YOUR_REGION:YOUR_ACCOUNT_ID:YOUR_TOPIC_NAME";
            PublishRequest publishRequest = PublishRequest.builder().message(eventJson)
                    .topicArn(snsTopicArn)
                    .build();
            PublishResponse publishResponse = snsClient.publish(publishRequest);

            //TODO: probable extention: Error handling required here for publish event flow in case it fails,
            // Report it to the API if required or report it to logger at-least for analytics
            return status;
        } catch (BusinessException exception) {
            logger.error(exception.getMessage(), exception);
            return exception.getMessage();
        }
    }
}
