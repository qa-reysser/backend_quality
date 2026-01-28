package com.quality.service.implement;

import com.quality.exception.resource.ResourceNotFoundAfterOperationException;
import com.quality.exception.resource.ResourceNotFoundByIdException;
import com.quality.repository.IGenericRepository;
import com.quality.service.IOperations;

import java.util.List;

public class OperationsImplement<T, ID> implements IOperations<T, ID> {
    
    protected IGenericRepository<T, ID> getRepo() {
        return null;
    }

    /**
     * Gets the resource type name for error messages.
     * Override this in subclasses to provide specific resource names.
     */
    protected String getResourceType() {
        return "Resource";
    }

    @Override
    public T save(T t) {
        return getRepo().save(t);
    }

    @Override
    public T update(T t, ID id) {
        getRepo().findById(id).orElseThrow(() -> 
            new ResourceNotFoundAfterOperationException(getResourceType(), id, "update"));
        return getRepo().save(t);
    }

    @Override
    public List<T> findAll() {
        return getRepo().findAll();
    }

    @Override
    public T findById(ID id) {
        return getRepo().findById(id).orElseThrow(() -> 
            new ResourceNotFoundByIdException(getResourceType(), id));
    }

    @Override
    public void delete(ID id) {
        getRepo().findById(id).orElseThrow(() -> 
            new ResourceNotFoundAfterOperationException(getResourceType(), id, "delete"));
        getRepo().deleteById(id);
    }
}
