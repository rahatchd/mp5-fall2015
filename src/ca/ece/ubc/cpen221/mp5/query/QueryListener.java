// Generated from Query.g4 by ANTLR 4.4

package ca.ece.ubc.cpen221.mp5.query;

import org.antlr.v4.runtime.misc.NotNull;
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link QueryParser}.
 */
public interface QueryListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link QueryParser#in}.
	 * @param ctx the parse tree
	 */
	void enterIn(@NotNull QueryParser.InContext ctx);
	/**
	 * Exit a parse tree produced by {@link QueryParser#in}.
	 * @param ctx the parse tree
	 */
	void exitIn(@NotNull QueryParser.InContext ctx);
	/**
	 * Enter a parse tree produced by {@link QueryParser#price}.
	 * @param ctx the parse tree
	 */
	void enterPrice(@NotNull QueryParser.PriceContext ctx);
	/**
	 * Exit a parse tree produced by {@link QueryParser#price}.
	 * @param ctx the parse tree
	 */
	void exitPrice(@NotNull QueryParser.PriceContext ctx);
	/**
	 * Enter a parse tree produced by {@link QueryParser#query}.
	 * @param ctx the parse tree
	 */
	void enterQuery(@NotNull QueryParser.QueryContext ctx);
	/**
	 * Exit a parse tree produced by {@link QueryParser#query}.
	 * @param ctx the parse tree
	 */
	void exitQuery(@NotNull QueryParser.QueryContext ctx);
	/**
	 * Enter a parse tree produced by {@link QueryParser#name}.
	 * @param ctx the parse tree
	 */
	void enterName(@NotNull QueryParser.NameContext ctx);
	/**
	 * Exit a parse tree produced by {@link QueryParser#name}.
	 * @param ctx the parse tree
	 */
	void exitName(@NotNull QueryParser.NameContext ctx);
	/**
	 * Enter a parse tree produced by {@link QueryParser#rating}.
	 * @param ctx the parse tree
	 */
	void enterRating(@NotNull QueryParser.RatingContext ctx);
	/**
	 * Exit a parse tree produced by {@link QueryParser#rating}.
	 * @param ctx the parse tree
	 */
	void exitRating(@NotNull QueryParser.RatingContext ctx);
	/**
	 * Enter a parse tree produced by {@link QueryParser#orExpr}.
	 * @param ctx the parse tree
	 */
	void enterOrExpr(@NotNull QueryParser.OrExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link QueryParser#orExpr}.
	 * @param ctx the parse tree
	 */
	void exitOrExpr(@NotNull QueryParser.OrExprContext ctx);
	/**
	 * Enter a parse tree produced by {@link QueryParser#range}.
	 * @param ctx the parse tree
	 */
	void enterRange(@NotNull QueryParser.RangeContext ctx);
	/**
	 * Exit a parse tree produced by {@link QueryParser#range}.
	 * @param ctx the parse tree
	 */
	void exitRange(@NotNull QueryParser.RangeContext ctx);
	/**
	 * Enter a parse tree produced by {@link QueryParser#atom}.
	 * @param ctx the parse tree
	 */
	void enterAtom(@NotNull QueryParser.AtomContext ctx);
	/**
	 * Exit a parse tree produced by {@link QueryParser#atom}.
	 * @param ctx the parse tree
	 */
	void exitAtom(@NotNull QueryParser.AtomContext ctx);
	/**
	 * Enter a parse tree produced by {@link QueryParser#category}.
	 * @param ctx the parse tree
	 */
	void enterCategory(@NotNull QueryParser.CategoryContext ctx);
	/**
	 * Exit a parse tree produced by {@link QueryParser#category}.
	 * @param ctx the parse tree
	 */
	void exitCategory(@NotNull QueryParser.CategoryContext ctx);
	/**
	 * Enter a parse tree produced by {@link QueryParser#andExpr}.
	 * @param ctx the parse tree
	 */
	void enterAndExpr(@NotNull QueryParser.AndExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link QueryParser#andExpr}.
	 * @param ctx the parse tree
	 */
	void exitAndExpr(@NotNull QueryParser.AndExprContext ctx);
}