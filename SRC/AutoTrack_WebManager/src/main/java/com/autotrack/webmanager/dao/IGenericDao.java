package com.autotrack.webmanager.dao;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;

import org.hibernate.SessionFactory;

@SuppressWarnings("all")
public interface IGenericDao {
	
	public SessionFactory getSessionFactory();
	public void setSessionFactory(SessionFactory sessionFactory);
	public void iniciarColecoes( Object objetoPai, String[] filhos ) throws SecurityException, NoSuchMethodException, IllegalArgumentException, IllegalAccessException, InvocationTargetException;
	public void reconnect(Object objeto);
	public void save(Object objeto) throws Exception;
	public <T> T getObjeto( Class< T > classe, Serializable id );
	public void update(Object objeto) throws Exception;
	public void delete(Object objeto) throws Exception;
	public void deleteAll(List listaObjetos) throws Exception;
	public List getAll(Class classe);
	public List getAll(Class classe, String campoOrderBy, int order);
	public List getAll(Class classe, int firstResult, int maxResults);
	public List getAll(Class classe, int firstResult, int maxResults, String campoOrderBy, int order);
	public List getAll(Class classe, String [][]filtros);
	public List getAll(Class classe, String [][]filtros, String campoOrderBy, int order);
	public List getPeloExemplo(Object example, boolean isEnableLike, boolean isIgnoreCase);
	public List getPeloExemplo(Object example, HashMap associacoes, boolean isEnableLike, boolean isIgnoreCase, boolean isExcludeZeros);
	/*public List getObjeto(String strQuery);*/
	public List getDadosDuplicados(Class classe, Long id, String campo, Object dado);
	public List getDadosDuplicados(Class classe, Long id, String campoPai, Object pai, String campo, Object dado);
	public void getRefresh(Object objeto);
	
}
 