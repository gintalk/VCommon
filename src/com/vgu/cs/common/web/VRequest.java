package com.vgu.cs.common.web;

import com.vgu.cs.common.logger.VLogger;
import com.vgu.cs.common.util.LoggingUtils;
import com.vgu.cs.common.util.ServletUtils;
import com.vgu.cs.common.util.StringUtils;
import com.vgu.cs.common.web.thrift.TStatusCode;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public abstract class VRequest implements IRequest {

    private static final Logger LOGGER = VLogger.getLogger(VRequest.class);
    private final HttpServletRequest _request;
    private final HttpServletResponse _response;
    private final VPath _path;
    private final long _currentTime;
    protected VModel<? extends IRequest> _model;
    private VResponse _apiResponse;

    public VRequest(HttpServletRequest req, HttpServletResponse res, VPath path, VModel<? extends IRequest> model) {
        _request = req;
        _response = res;
        _path = path;
        _model = model;
        _currentTime = System.currentTimeMillis();
    }

    @Override
    public void doRequest() {
        try {
            if (!_path.isValid()) {
                LOGGER.error(LoggingUtils.buildLog("Invalid path", _path));
                return;
            }
            _doProcess();
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage(), ex);
        }
    }

    @Override
    public VResponse getApiResponse() {
        return _apiResponse;
    }

    @Override
    public long getCurrentTime() {
        return _currentTime;
    }

    @Override
    public VPath getPath() {
        return _path;
    }

    @Override
    public HttpServletRequest getHRequest() {
        return _request;
    }

    @Override
    public HttpServletResponse getHResponse() {
        return _response;
    }

    protected boolean outAndClose(HttpServletRequest req, HttpServletResponse res, VResponse apiResponse) {
        if (apiResponse == null) {
            LOGGER.error(LoggingUtils.buildLog("Empty API response"));
            return false;
        }

        String message = apiResponse.toString();
        if (StringUtils.isNullOrEmpty(message)) {
            LOGGER.error(LoggingUtils.buildLog("Empty API response content", apiResponse));
            return false;
        }
        ServletUtils.prepareResponseHeader(res);

        return ServletUtils.printString(req, res, message);
    }

    protected VResponse doProcess() {
        if (_model == null) {
            return new VResponse(TStatusCode.FAIL);
        }

        return _model.doProcess(this);
    }

    private void _doProcess() {
        try {
            _apiResponse = doProcess();
            outAndClose(_request, _response, _apiResponse);
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage(), ex);
        }
    }
}
