package io.pivotal.microservices.products;

import java.util.List;

import org.junit.Before;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import io.pivotal.microservices.productcatalog.controller.ProductsController;
import io.pivotal.microservices.productcatalog.model.Product;
import io.pivotal.microservices.productcatalog.repository.ProductRepository;

public class ProductsControllerTests extends AbstractProductControllerTests {

	protected static final Product theProduct = new Product(PRODUCT_NAME, PRODUCT_TYPE, PRODUCT_PRICE);

	protected static class TestProductRepository implements ProductRepository {

		@Override
		public Iterable<Product> findAll(Sort sort) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Page<Product> findAll(Pageable pageable) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public <S extends Product> S save(S entity) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public <S extends Product> Iterable<S> save(Iterable<S> entities) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Product findOne(Long id) {
			return theProduct;
		}

		@Override
		public boolean exists(Long id) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public Iterable<Product> findAll() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Iterable<Product> findAll(Iterable<Long> ids) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long count() {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public void delete(Long id) {
			// TODO Auto-generated method stub

		}

		@Override
		public void delete(Product entity) {
			// TODO Auto-generated method stub

		}

		@Override
		public void delete(Iterable<? extends Product> entities) {
			// TODO Auto-generated method stub

		}

		@Override
		public void deleteAll() {
			// TODO Auto-generated method stub

		}

		@Override
		public List<Product> findByType(String productType) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public List<Product> findByName(String productName) {
			// TODO Auto-generated method stub
			return null;
		}
	}

	protected TestProductRepository testRepo = new TestProductRepository();

	@Before
	public void setup() {
		productsController = new ProductsController(testRepo);
	}
}
