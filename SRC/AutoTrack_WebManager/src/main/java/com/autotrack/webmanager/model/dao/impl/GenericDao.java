package com.autotrack.webmanager.model.dao.impl;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Example;
import org.hibernate.criterion.Expression;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Property;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.autotrack.webmanager.model.dao.IGenericDao;

@SuppressWarnings("all")
public abstract class GenericDao implements IGenericDao {

	@Resource(name = "sessionFactory")
	private SessionFactory sessionFactory;

	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	public void clearSession() {
		this.sessionFactory.getCurrentSession().flush();
		this.sessionFactory.getCurrentSession().clear();
	}

	@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
	public void iniciarColecoes(Object objetoPai, String[] filhos)
			throws SecurityException, NoSuchMethodException,
			IllegalArgumentException, IllegalAccessException,
			InvocationTargetException {

		this.sessionFactory.getCurrentSession().refresh(objetoPai);

		for (String filho : filhos) {

			// M�todo que retorna uma cole��o
			Method metodo = objetoPai.getClass().getMethod(
					"get" + filho.substring(0, 1).toUpperCase()
							+ filho.substring(1));

			this.sessionFactory.getCurrentSession().load(
					(metodo.invoke(objetoPai)),
					(metodo.invoke(objetoPai).hashCode()));
		}
	}

	@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
	public void reconnect(Object objeto) {
		sessionFactory.getCurrentSession().refresh(objeto);
		sessionFactory.getCurrentSession().load(objeto, objeto.hashCode());
	}

	/**
	 * Executa opera��o de inclus�o
	 * 
	 * @throws Exception
	 * @throws Exception
	 */
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public void save(Object objeto) throws Exception {
		try {
			this.sessionFactory.getCurrentSession().save(objeto);
			this.sessionFactory.getCurrentSession().persist(objeto);
			this.sessionFactory.getCurrentSession().flush();

		} catch (Exception ex) {
			ex.printStackTrace();
			tratarErro(ex, "inclusão");
		}

	}
	
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public void saveOrUpdate(Object objeto) throws Exception {
		try {
			this.sessionFactory.getCurrentSession().saveOrUpdate(objeto);
			this.sessionFactory.getCurrentSession().persist(objeto);
			this.sessionFactory.getCurrentSession().flush();

		} catch (Exception ex) {
			ex.printStackTrace();
			tratarErro(ex, "inclusão");
		}

	}

	/**
	 * Executa opera��o de altera��o
	 * 
	 * @throws Exception
	 */
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public void update(Object objeto) throws Exception {
		try {
			this.sessionFactory.getCurrentSession().update(objeto);
			this.sessionFactory.getCurrentSession().persist(objeto);
			this.sessionFactory.getCurrentSession().flush();

		} catch (Exception ex) {
			this.sessionFactory.getCurrentSession().cancelQuery();
			ex.printStackTrace();
			tratarErro(ex, "altera��o");

		}
	}

	/**
	 * Executa opera��o de exclus�o
	 * 
	 * @throws Exception
	 */
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public void delete(Object objeto) throws Exception {
		try {
			this.sessionFactory.getCurrentSession().delete(objeto);
			this.sessionFactory.getCurrentSession().flush();

		} catch (Exception ex) {
			ex.printStackTrace();
			tratarErro(ex, "exclus�o");
		}
	}

	/**
	 * Executa opera��o de exclus�o
	 * 
	 * @throws Exception
	 */
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public void deleteAll(List listaObjetos) throws Exception {
		try {
			for (Object object : listaObjetos) {
				this.sessionFactory.getCurrentSession().delete(object);
			}
			this.sessionFactory.getCurrentSession().flush();

		} catch (Exception ex) {
			ex.printStackTrace();
			tratarErro(ex, "exclus�o");
		}
	}

	/**
	 * Recupera um objeto a partir de um identificador
	 */
	@SuppressWarnings("unchecked")
	@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
	public <T> T getObjeto(Class<T> classe, Serializable id) {

		return (T) this.sessionFactory.getCurrentSession().get(classe, id);
	}

	/**
	 * Listagem dos objetos da classe informada
	 */
	@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
	public List getAll(Class classe) {
		return this.sessionFactory.getCurrentSession().createCriteria(classe)
				.list();
	}

	/**
	 * Listagem dos objetos da classe informada
	 * 
	 * @param c
	 *            Classe dos objetos
	 * @param campoOrderBy
	 *            Nome do atributo que ser� ordenado
	 * @param order
	 *            Ordena��o da lista. Informe 1 para ascendente e outro valor
	 *            para descendente
	 * @return listagem dos objetos da classe informada
	 */
	@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
	public List getAll(Class classe, String campoOrderBy, int order) {

		Criteria cri = this.sessionFactory.getCurrentSession().createCriteria(
				classe);
		if (campoOrderBy != null && campoOrderBy.trim().length() > 0) {
			if (order == 1)
				cri.addOrder(Order.asc(campoOrderBy));
			else
				cri.addOrder(Order.desc(campoOrderBy));
		}
		return cri.list();

	}

	/**
	 * Listagem dos objetos da classe informada com paginaca
	 * 
	 * @param c
	 *            - Classe dos objetos
	 * @param firstResult
	 *            - Inicio da listagem
	 * @param maxResults
	 *            - quantidade maxima de objetos retornados
	 * @return listagem dos objetos da classe informada
	 */
	@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
	public List getAll(Class classe, int firstResult, int maxResults) {

		Criteria criteria = this.sessionFactory.getCurrentSession()
				.createCriteria(classe);
		criteria.setFirstResult(firstResult);
		criteria.setMaxResults(maxResults);
		return criteria.list();
	}

	/**
	 * Listagem dos objetos da classe informada com paginaca
	 * 
	 * @param c
	 *            - Classe dos objetos
	 * @param firstResult
	 *            - Inicio da listagem
	 * @param maxResults
	 *            - quantidade maxima de objetos retornados
	 * @param campoOrderBy
	 *            Nome do atributo que ser� ordenado
	 * @param order
	 *            Ordena��o da lista. Informe 1 para ascendente e outro valor
	 *            para descendente
	 * @return listagem dos objetos da classe informada
	 */
	@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
	public List getAll(Class classe, int firstResult, int maxResults,
			String campoOrderBy, int order) {

		Criteria criteria = this.sessionFactory.getCurrentSession()
				.createCriteria(classe);
		criteria.setFirstResult(firstResult);
		criteria.setMaxResults(maxResults);
		if (campoOrderBy != null && campoOrderBy.trim().length() > 0) {
			if (order == 1)
				criteria.addOrder(Order.asc(campoOrderBy));
			else
				criteria.addOrder(Order.desc(campoOrderBy));
		}
		return criteria.list();
	}

	/**
	 * Listagem por filtros
	 * 
	 * @param c
	 *            - Classe dos objetos
	 * @param filtros
	 *            matriz que contem na primeira coluna o campo a ser filtrado e
	 *            na segunda coluna o dado do filtro
	 * @return listagem retornada da consulta com os filtros
	 */
	@SuppressWarnings("deprecation")
	@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
	public List getAll(Class classe, String[][] filtros) {

		Criteria cri = this.sessionFactory.getCurrentSession().createCriteria(
				classe);
		for (int i = 0; i < filtros.length; i++) {
			cri.add(Expression.like(filtros[i][0], filtros[i][1],
					MatchMode.ANYWHERE).ignoreCase());
		}
		return cri.list();
	}

	/**
	 * Listagem por filtros
	 * 
	 * @param c
	 *            - Classe dos objetos
	 * @param filtros
	 *            matriz que contem na primeira coluna o campo a ser filtrado e
	 *            na segunda coluna o dado do filtro
	 * @return listagem retornada da consulta com os filtros
	 */
	@SuppressWarnings("deprecation")
	@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
	public List getAll(Class classe, String[][] filtros, String campoOrderBy,
			int order) {

		Criteria cri = this.sessionFactory.getCurrentSession().createCriteria(
				classe);
		for (int i = 0; i < filtros.length; i++) {
			cri.add(Expression.like(filtros[i][0], filtros[i][1],
					MatchMode.ANYWHERE).ignoreCase());
		}
		if (campoOrderBy != null && campoOrderBy.trim().length() > 0) {
			if (order == 1)
				cri.addOrder(Order.asc(campoOrderBy));
			else
				cri.addOrder(Order.desc(campoOrderBy));
		}

		return cri.list();
	}

	/**
	 * Metodo para listar objetos semelhantes ao Object example
	 * 
	 * @param exemplo
	 *            : objeto Example
	 * */
	@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
	public List getPeloExemplo(Object example) {

		Criteria criteria = this.sessionFactory.getCurrentSession()
				.createCriteria(example.getClass());
		Example sample = Example.create(example);
		sample.enableLike(MatchMode.ANYWHERE);
		sample.excludeZeroes();
		criteria.add(sample);
		return criteria.list();
	}

	/**
	 * Metodo para listar objetos semelhantes ao Object example
	 * 
	 * @param exemplo
	 *            : objeto Example
	 * @param isEnableLike
	 *            : True se � para ativar o "Like" na consulta, false para
	 *            desativar
	 * @param isIgnoreCase
	 *            : True se � para ignorar mai�sculas e min�sculas na consulta,
	 *            false para case sensitive
	 * */
	@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
	public List getPeloExemplo(Object example, boolean isEnableLike,
			boolean isIgnoreCase) {

		Criteria criteria = this.sessionFactory.getCurrentSession()
				.createCriteria(example.getClass());
		Example sample = Example.create(example);
		if (isEnableLike)
			sample.enableLike(MatchMode.ANYWHERE);

		if (isIgnoreCase)
			sample.ignoreCase();

		sample.excludeZeroes();
		criteria.add(sample);
		return criteria.list();

	}

	/**
	 * Metodo para listar objetos semelhantes ao Object example
	 * 
	 * @param exemplo
	 *            : objeto Example
	 * @param isEnableLike
	 *            : True se � para ativar o "Like" na consulta, false para
	 *            desativar
	 * @param isIgnoreCase
	 *            : True se � para ignorar mai�sculas e min�sculas na consulta,
	 *            false para case sensitive
	 * @param isExcludeZeros
	 *            : True se � para ignorar valores zero, n�o adicionando na
	 *            consulta
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
	public List getPeloExemplo(Object example, HashMap associacoes,
			boolean isEnableLike, boolean isIgnoreCase, boolean isExcludeZeros) {

		Criteria criteria = this.sessionFactory.getCurrentSession()
				.createCriteria(example.getClass());

		Example sample = Example.create(example);
		if (isEnableLike)
			sample.enableLike(MatchMode.ANYWHERE);

		if (isIgnoreCase)
			sample.ignoreCase();

		if (isExcludeZeros)
			sample.excludeZeroes();

		criteria.add(sample);

		if (associacoes.size() > 0) {
			Set keys = associacoes.keySet();

			for (Iterator iter = keys.iterator(); iter.hasNext();) {
				String key = (String) iter.next();
				criteria.add(Property.forName(key).eq(associacoes.get(key)));
			}
		}

		return criteria.list();

	}

	/**
	 * Recupera uma lista de objetos segundo a Query informada
	 * 
	 * @param strQuery
	 *            - consulta
	 * @return lista de objetos retornados pela consulta
	 */
	/*
	 * public List getObjeto(String strQuery){
	 * 
	 * Query query = this.getSession().createQuery(strQuery); return
	 * query.list();
	 * 
	 * }
	 */

	/**
	 * Recupera o Objeto na base de dados
	 * 
	 * @param objeto
	 *            Objeto a ser recuperado
	 * */
	@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
	public void getRefresh(Object objeto) {

		this.sessionFactory.getCurrentSession().refresh(objeto);

	}

	/**
	 * Verifica se j� existe um objeto cadastrado
	 * 
	 * @param c
	 *            Classe do objeto
	 * @param id
	 *            O identificador deve ser informado quando for uma altera��o,
	 *            em caso de inclus�o deve-se informar null
	 * @param campo
	 *            Campo do objeto
	 * @param dado
	 *            Dado do objeto
	 * @return boolean
	 */
	@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
	public List getDadosDuplicados(Class classe, Long id, String campo,
			Object dado) {

		Criteria criterio = this.sessionFactory.getCurrentSession()
				.createCriteria(classe);
		criterio.add(Expression.eq(campo, dado));
		if (id != null)
			criterio.add(Expression.ne("id", id));
		return criterio.list();

	}

	/**
	 * Verifica se j� existe um objeto cadastrado
	 * 
	 * @param c
	 *            Classe do objeto
	 * @param id
	 *            O identificador deve ser informado quando for uma altera��o,
	 *            em caso de inclus�o deve-se informar null
	 * @param campoPai
	 *            Campo Pai do objeto
	 * @param pai
	 *            Pai do objeto e tem que ser informado quando for um cadastrado
	 *            de pai e filho
	 * @param campo
	 *            Campo do objeto
	 * @param dado
	 *            Dado do objeto
	 * @return boolean
	 */
	@Transactional(readOnly = true, propagation = Propagation.REQUIRED)
	public List getDadosDuplicados(final Class classe, final Long id,
			final String campoPai, final Object pai, final String campo,
			final Object dado) {

		Criteria criterio = this.sessionFactory.getCurrentSession()
				.createCriteria(classe);
		criterio.add(Expression.eq(campo, dado));
		if (pai != null)
			criterio.add(Expression.eq(campoPai, pai));
		else
			criterio.add(Expression.isNull(campoPai));

		if (id != null)
			criterio.add(Expression.ne("id", id));
		return criterio.list();

	}

	/**
	 * Responsável pelo tratamento de erros
	 * 
	 * @param ex
	 *            - Exceção levantada pelo sistema.
	 * @param acao
	 *            - Ação atual do sistema: inclusão, alteração, exclusão, etc.
	 * @throws Exception
	 */
	private void tratarErro(Exception ex, String acao) throws Exception {
		String msgException = "";

		if (ex.getCause() != null)
			msgException = ((SQLException) ex.getCause()).getNextException()
					.getMessage().toUpperCase();
		else
			msgException = ex.getMessage().toUpperCase();

		// Erro Chave duplicada
		if (msgException.contains("DUPLICAR CHAVE VIOLA")
				|| msgException.contains("DUPLICATE KEY VIOLATES")) {

			throw new Exception("Registro cadastrado anteriormente.");
		}
		// Erro Campos obrigatórios não informados
		else if (msgException
				.contains("NOT-NULL PROPERTY REFERENCES A NULL OR TRANSIENT VALUE")) {

			throw new Exception("Campos obrigat�rios n�o foram informados.");
		}
		// Erro na exclusão dos dados, por estar referenciado por outras tabelas
		else if ((msgException.contains("EXCLUS")
				&& msgException.contains("VIOLA") && msgException
					.contains("CHAVE ESTRANGEIRA"))
				|| (msgException.contains("DELETE ON TABLE") && msgException
						.contains("VIOLATES FOREIGN KEY CONSTRAINT"))) {

			throw new Exception(
					"O(s) item(ns) selecionado(s) est�(�o) associado(s) a outros cadastros.");
		}
		// Erro de Foreign Key
		else if ((msgException.contains("INSER")
				&& msgException.contains("ATUALIZA")
				&& msgException.contains("VIOLA") && msgException
					.contains("CHAVE ESTRANGEIRA"))
				|| (msgException.contains("INSERT OR UPDATE ON TABLE") && msgException
						.contains("VIOLATES FOREIGN KEY CONSTRAINT"))) {

			throw new Exception(
					"O cadastro faz refer�ncia a um registro inexistente.");
		}
		// Demais erros
		else {
			throw new Exception("Erro na " + acao + " dos dados.");
		}
	}

}
