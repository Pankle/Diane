package cn.ajjj.reflection;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

/**
 * Created by Pankle on 2018/12/3.
 */
@Data
@AllArgsConstructor
public class RequestData implements Serializable {
    private String pop;
    private String loanPurpose;
    private String withdrawApplyId;
    private String privacyBatchId;
    private String withdrawAmt;

    public RequestData(){
        super();
    }

}
