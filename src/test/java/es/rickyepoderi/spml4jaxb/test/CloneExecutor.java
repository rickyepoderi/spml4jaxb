/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.rickyepoderi.spml4jaxb.test;

import es.rickyepoderi.spml4jaxb.accessor.BaseRequestAccessor;
import es.rickyepoderi.spml4jaxb.accessor.RequestAccessor;
import es.rickyepoderi.spml4jaxb.builder.ResponseBuilder;
import es.rickyepoderi.spml4jaxb.test.clone.CloneRequestAccessor;
import es.rickyepoderi.spml4jaxb.test.clone.CloneResponseBuilder;
import es.rickyepoderi.spml4jaxb.user.AlreadyExistsException;
import es.rickyepoderi.spml4jaxb.user.InvalidUserException;
import es.rickyepoderi.spml4jaxb.user.ManagerException;
import es.rickyepoderi.spml4jaxb.user.User;
import es.rickyepoderi.spml4jaxb.user.UserManager;

/**
 *
 * @author ricky
 */
public class CloneExecutor extends AsyncSpmlBaseExecutor {

    private UserManager um;
    
    public CloneExecutor(UserManager um, WorkQueue queue) {
        super(queue);
        this.um = um;
    }
    
    @Override
    public BaseRequestAccessor specificAccessor(RequestAccessor request) {
        return request.asAccessor(CloneRequestAccessor.emptyCloneAccessor());
    }
    
    @Override
    public ResponseBuilder realExecute(WorkQueue.WorkItem item) {
        CloneResponseBuilder builder = CloneResponseBuilder.builderForClone().requestId(item.getId()).success();
        CloneRequestAccessor req = item.getRequestAccessor().asUnknown().asAccessor(CloneRequestAccessor.emptyCloneAccessor());
        try {
            User cloned = new User();
            if (req.isTargetId(ListTargetsExecutor.DSML_TARGET_ID)) {
                // dsml
                // create the cloned user with the data sent
                AddExecutor.spml2User(cloned, req.getDsmlAttributes());
                // read the data using the template id
                String templateUid = req.getTemplateId();
                if (templateUid != null) {
                    um.clone(templateUid, cloned);
                    builder.psoId(cloned.getUid()).psoTargetId(req.getTargetId());
                    if (req.isReturnData() || req.isReturnEverything()) {
                        builder.dsmlAttribute(AddExecutor.user2Dsml(cloned));
                    }
                } else {
                    builder.failure().invalidIdentifier().errorMessage("The templateId is compulsory for cloning a user.");
                }
            } else if (req.isTargetId(ListTargetsExecutor.XSD_TARGET_ID)) {
                // xsd
                cloned = (User) req.getXsdObject(User.class);
                String templateUid = req.getTemplateId();
                if (templateUid != null) {
                    um.clone(templateUid, cloned);
                    builder.psoId(cloned.getUid()).psoTargetId(req.getTargetId());
                    if (req.isReturnData() || req.isReturnEverything()) {
                        builder.xsdObject(cloned);
                    }
                } else {
                    builder.failure().invalidIdentifier().errorMessage("The templateId is compulsory for cloning a user.");
                }
            } else {
                builder.failure()
                        .unsupportedIdentifierType()
                        .errorMessage("Invalid targetID!");
            }
        } catch (InvalidUserException e) {
            builder.failure()
                    .malformedRequest()
                    .errorMessage(e.getMessage());
        } catch (AlreadyExistsException e) {
            builder.failure()
                    .alreadyExists()
                    .errorMessage(e.getMessage());
        } catch (ManagerException e) {
            builder.failure()
                    .customError()
                    .errorMessage(e.getMessage());
        }
        queue.finish(item, builder);
        return builder;
    }
    
}
