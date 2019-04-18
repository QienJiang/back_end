package cse308.map.server;

import cse308.map.repository.PrecinctRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PrecinctService {
    @Autowired
    private PrecinctRepository precinctRepository;
}
