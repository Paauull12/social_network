package org.example.repository.filerepo;

import org.example.domain.Entity;
import org.example.domain.validator.Validator;
import org.example.repository.InMemoryRepo;

import java.io.*;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public abstract class AbstractFileRepo<ID, E extends Entity<ID>> extends InMemoryRepo<ID, E> {

    protected String fileName;

    public AbstractFileRepo(Validator<E> validator, String fileName) {
        super(validator);
        this.fileName = fileName;
        loadData();
    }

    private void loadData(){
        try(BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String newLine;

            while((newLine = reader.readLine()) != null){
                System.out.println(newLine);
                List<String> listaInput = Arrays.asList(newLine.split(";"));
                E entity = extractEntity(listaInput);
                super.save(entity);
            }

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public abstract E extractEntity(List<String> listaInput);

    protected abstract String createEntity(E entity);

    @Override
    public Optional<E> save(E entity){
        Optional<E> entit = super.save(entity);
        if(entit.isEmpty()){
            writeToFile();
        }
        return entit;
    }

    @Override
    public Optional<E> delete(ID id) {
        Optional<E> result = super.delete(id);
        if(result.isPresent())
            writeToFile();
        return result;
    }

    @Override
    public Optional<E> update(E entity) {
        Optional<E> result = super.update(entity);
        if(result.isPresent())
            writeToFile();
        return result;
    }

    protected void writeToFile() {

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName, false))) {

            for (Map.Entry<ID, E> entry : lista.entrySet()) {
                E entity = entry.getValue();
                writer.write(createEntity(entity));
                writer.newLine();
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
