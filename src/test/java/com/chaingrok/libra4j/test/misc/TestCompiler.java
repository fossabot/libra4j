package com.chaingrok.libra4j.test.misc;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;

import org.junit.FixMethodOrder;
import org.junit.runners.MethodSorters;

import com.chaingrok.libra4j.misc.Compiler;
import com.chaingrok.libra4j.misc.Libra4jConfig;
import com.chaingrok.libra4j.test.TestConfig;
import com.chaingrok.libra4j.types.Transaction;
import com.chaingrok.libra4j.types.Transaction.Type;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestCompiler {
	
	public static final String LIBRA_DIR = TestConfig.HOME_DIR_PATH + File.separator + "libra-master-2019-10-17"; // libra project dir
	
	//Those tests fail on Travis CI as libra project is required in order to have compiler
	
	//@Test
	public void test001CompileGetVersion() {
		Compiler compiler = new Compiler(LIBRA_DIR);
		assertTrue(compiler.getVersion().startsWith(Compiler.VERSION_PREFIX));
		assertEquals(0,compiler.getMajorVersion());
		assertEquals(1,compiler.getMinorVersion());
	}
	
	//@Test
	public void test002CompileStandardTransactionScripts() { //as per /language/stblib/transaction_scripts
		for(Type type  : Transaction.Type.values()) {
			if (!Transaction.Type.UNKNOWN.equals(type)) {
				String mvirSourceCode = type.getMvirSourceCode();
				String mvirFilepath = Libra4jConfig.MVIR_DIR + File.separator + mvirSourceCode + ".mvir";
				String mvbcFilepath = ".mv";
				Compiler compiler = new Compiler(LIBRA_DIR);
				System.out.println("Print commmand: " + compiler.createCompileCommand(mvirFilepath));
				File mvbcFile = compiler.compile(mvirFilepath);
				assertTrue(mvbcFile.exists());
				assertTrue(mvbcFile.getAbsolutePath().endsWith(mvbcFilepath));
			}
		}
	}

}
