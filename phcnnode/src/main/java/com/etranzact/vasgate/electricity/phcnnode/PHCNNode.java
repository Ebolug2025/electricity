package com.etranzact.vasgate.electricity.phcnnode;

import com.etranzact.vasgate.electricity.phcnnode.dto.*;

public abstract class PHCNNode {

    public abstract ElectricityQueryResponse query(ElectricityQueryRequest request);

    public abstract ElectricityProcessResponse process(ElectricityProcessRequest request);

    public abstract ElectricityProcessResponse reQuery(ElectricityReQueryRequest request);

    public abstract PingResponse ping(ElectricityQueryRequest request);
}
