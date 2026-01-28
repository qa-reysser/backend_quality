package com.quality.service.implement;

import com.quality.exception.validation.DuplicateFieldException;
import com.quality.model.Priority;
import com.quality.repository.IGenericRepository;
import com.quality.repository.IPriorityRepository;
import com.quality.service.IPriorityService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PriorityServiceImplement extends OperationsImplement<Priority, Integer> implements IPriorityService {
    private final IPriorityRepository repository;

    @Override
    protected IGenericRepository<Priority, Integer> getRepo() {
        return repository;
    }

    @Override
    protected String getResourceType() {
        return "Priority";
    }

    @Override
    public Priority save(Priority priority) {
        checkDuplicateName(priority.getName(), null);
        return super.save(priority);
    }

    @Override
    public Priority update(Priority priority, Integer id) {
        checkDuplicateName(priority.getName(), id);
        return super.update(priority, id);
    }

    /**
     * Check if a Priority with the given name already exists.
     * 
     * @param name the name to check
     * @param excludeId the ID to exclude from the check (null for save, id for update)
     * @throws DuplicateFieldException if a duplicate name is found
     */
    private void checkDuplicateName(String name, Integer excludeId) {
        boolean exists = (excludeId == null)
                ? repository.existsByName(name)
                : repository.existsByNameAndIdPriorityNot(name, excludeId);

        if (exists) {
            throw new DuplicateFieldException("name", name);
        }
    }
}
